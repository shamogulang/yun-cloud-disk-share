package com.example.share.service.impl;

import com.example.share.feign.FileFeign;
import com.example.share.feign.UserFeign;
import com.example.share.mapper.ShareMapper;
import com.example.share.model.*;
import com.example.share.service.ShareService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

import com.example.share.mapper.ShareContentMapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.Random;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ShareServiceImpl implements ShareService {
    @Resource
    private ShareMapper shareMapper;

    @Resource
    private ShareContentMapper shareContentMapper;

    @Resource
    private FileFeign fileFeign;
    @Resource
    private UserFeign userFeign;

    @Value("${share.url:''}")
    private String shareUrl;

    @Override
    public Share getById(Long id) {
        return shareMapper.selectById(id);
    }

    @Override
    public Share getByShareUrl(String shareUrl) {
        return shareMapper.selectByShareUrl(shareUrl);
    }

    @Override
    @Transactional
    public CreateShareResponse create(CreateShareRequest request) {
        // 1. 参数校验
        boolean hasFile = request.getCoIDLst() != null && !request.getCoIDLst().isEmpty();
        boolean hasDir = request.getCaIDLst() != null && !request.getCaIDLst().isEmpty();
        if (!hasFile && !hasDir) {
            throw new IllegalArgumentException("coIDLst、caIDLst至少一个不为空");
        }
        // 2. 生成外链ID、URL、密码
        String linkId = UUID.randomUUID().toString().replace("-", "");
        String linkUrl = shareUrl + linkId;
        String passwd = null;
        if (request.getEncrypt() != null && request.getEncrypt() == 1) {
            passwd = String.format("%04d", new Random().nextInt(10000));
        }
        // 3. 构建Share对象
        Share share = new Share();
        share.setShareUrl(linkUrl);
        share.setExtractCode(passwd);
        // 计算expireTime（Java8 API）
        Date expireTime = null;
        if (request.getPeriod() != null && request.getPeriod() > 0) {
            int unit = (request.getPeriodUnit() != null) ? request.getPeriodUnit() : 1;
            int days = request.getPeriod() * unit;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expire = now.plusDays(days);
            expireTime = Date.from(expire.atZone(ZoneId.systemDefault()).toInstant());
        }
        share.setExpireTime(expireTime);
        share.setUserId(request.getUserId());
        share.setStatus(1);
        share.setEncrypt(request.getEncrypt());
        share.setPubType(request.getPubType());
        share.setPeriod(request.getPeriod());
        share.setPeriodUnit(request.getPeriodUnit());
        share.setPasswd(passwd);
        share.setLinkId(linkId);
        share.setLinkUrl(linkUrl);
        shareMapper.insert(share);
        // 4. 构建ShareContent并插入
        Long shareId = share.getId();
        ArrayList<String> objIds = new ArrayList<>();
        if (hasFile) {
            for (Long fileId : request.getCoIDLst()) {
                ShareContent content = new ShareContent();
                content.setShareId(shareId);
                content.setObjId(fileId);
                content.setObjType("file");
                shareContentMapper.insert(content);
                objIds.add(String.valueOf(fileId));
            }
        }
        if (hasDir) {
            for (Long dirId : request.getCaIDLst()) {
                ShareContent content = new ShareContent();
                content.setShareId(shareId);
                content.setObjId(dirId);
                content.setObjType("dir");
                shareContentMapper.insert(content);
                objIds.add(String.valueOf(dirId));
            }
        }
        String objID = String.join(",", objIds);
        // 5. 返回响应
        CreateShareResponse resp = new CreateShareResponse();
        resp.setObjID(objID);
        resp.setPasswd(passwd);
        resp.setLinkID(linkId);
        resp.setLinkUrl(linkUrl);
        return resp;
    }

    @Override
    public int update(Share share) {
        return shareMapper.update(share);
    }

    @Override
    public List<Share> getByUserId(Long userId) {
        return shareMapper.selectByUserId(userId);
    }

    @Override
    public Result<List<FileInfoWithThumbnails>> verify(String linkId, String passwd) {
        Share share = shareMapper.selectByLinkId(linkId);
        if (share == null) {
            return Result.errorCode("分享不存在", 404);
        }

        if (share.getEncrypt() != null && share.getEncrypt() == 1) {
            if (passwd == null || !passwd.equals(share.getPasswd())) {
                return Result.error("密码错误");
            }
        }

        if (share.getStatus() != null && share.getStatus() == 0) {
            return Result.errorCode("分享已经被取消", 405);
        }

        // 查询share_content，获取所有文件ID
        List<ShareContent> contents = shareContentMapper.selectByShareId(share.getId());
        List<Long> fileIds = new ArrayList<>();
        for (ShareContent c : contents) {
            fileIds.add(c.getObjId());
        }
        Result<List<FileInfoWithThumbnails>> userAuthDataResult = fileFeign.listByIds(new ListByIdsReq(fileIds,share.getUserId()));
        int code = userAuthDataResult.getCode();
        if(code != 200){
            return Result.errorCode(userAuthDataResult.getMsg(), userAuthDataResult.getCode());
        }
        return Result.success(userAuthDataResult.getData());
    }

    @Override
    public Result<ShareDetailDTO> getShareDetail(String linkId) {
        Share share = shareMapper.selectByLinkId(linkId);
        if (share == null) {
            return Result.error("分享不存在");
        }
        Result<UserDto> userDto = userFeign.getUserInfo(share.getUserId());
        ShareDetailDTO dto = new ShareDetailDTO();
        dto.setLinkId(share.getLinkId());
        dto.setLinkUrl(share.getLinkUrl());
        dto.setUserId(share.getUserId());
        dto.setUsername(userDto.getData().getUsername());
        dto.setCreateTime(share.getCreateTime());
        dto.setExpireTime(share.getExpireTime());
        dto.setPubType(share.getPubType());
        dto.setEncrypt(share.getEncrypt());
        return Result.success(dto);
    }

    // mock feign
    private String mockGetUsernameByUserId(Long userId) {
        return "user_" + userId;
    }

    @Override
    @Transactional
    public Result<Boolean> cancelByLinkId(String linkId) {
        Share share = shareMapper.selectByLinkId(linkId);
        if (share == null) {
            return Result.error("分享不存在");
        }
        // 软删除：只更新status字段
        int rows = shareMapper.updateStatusByLinkId(linkId, 0); // 0表示已取消
        return rows > 0 ? Result.success(true) : Result.error("取消分享失败");
    }

    @Override
    public Result<Integer> countByUserId(Long userId) {
        int count = shareMapper.countByUserId(userId);
        return Result.success(count);
    }
} 