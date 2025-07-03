package com.example.share.service;

import com.example.share.model.Share;
import java.util.List;
import com.example.share.model.CreateShareRequest;
import com.example.share.model.CreateShareResponse;
import com.example.share.model.FileInfoDTO;
import com.example.share.model.Result;
import com.example.share.model.ShareDetailDTO;

public interface ShareService {
    Share getById(Long id);
    Share getByShareUrl(String shareUrl);
    int update(Share share);
    List<Share> getByUserId(Long userId);
    CreateShareResponse create(CreateShareRequest request);
    Result<List<FileInfoDTO>> verify(String linkId, String passwd);
    Result<ShareDetailDTO> getShareDetail(String linkId);
    Result<Boolean> cancelByLinkId(String linkId);
    Result<Integer> countByUserId(Long userId);
} 