package com.example.share.controller;

import com.example.share.model.*;
import com.example.share.service.ShareService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/share")
public class ShareController {
    @Resource
    private ShareService shareService;

    @GetMapping("/{id}")
    public Result<Share> getById(@PathVariable Long id) {
        return Result.success(shareService.getById(id));
    }

    @GetMapping("/url/{shareUrl}")
    public Result<Share> getByShareUrl(@PathVariable String shareUrl) {
        return Result.success(shareService.getByShareUrl(shareUrl));
    }

    @GetMapping("/user/{userId}")
    public Result<List<Share>> getByUserId(@PathVariable Long userId) {
        return Result.success(shareService.getByUserId(userId));
    }

    @PostMapping("/create")
    public Result<CreateShareResponse> create(@RequestAttribute("userId") Long userId,@RequestBody CreateShareRequest request) {
        request.setUserId(userId);
        return Result.success(shareService.create(request));
    }

    @PostMapping("/update")
    public Result<Integer> update(@RequestBody Share share) {
        return Result.success(shareService.update(share));
    }

    @PostMapping("/verify")
    public Result<List<FileInfoWithThumbnails>> verify(@RequestBody ShareVerifyRequest req) {
        return shareService.verify(req.getLinkId(), req.getPasswd());
    }

    @GetMapping("/detail/{linkId}")
    public Result<ShareDetailDTO> getShareDetail(@PathVariable String linkId) {
        return shareService.getShareDetail(linkId);
    }

    @PostMapping("/cancel/{linkId}")
    public Result<Boolean> cancelByLinkId(@PathVariable String linkId) {
        return shareService.cancelByLinkId(linkId);
    }

    @GetMapping("/count")
    public Result<Integer> countByUserId(@RequestAttribute("userId") Long userId) {
        return shareService.countByUserId(userId);
    }
} 