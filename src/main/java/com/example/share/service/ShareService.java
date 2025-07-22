package com.example.share.service;

import com.example.share.model.*;

import java.util.List;

public interface ShareService {
    Share getById(Long id);
    Share getByShareUrl(String shareUrl);
    int update(Share share);
    List<Share> getByUserId(Long userId);
    CreateShareResponse create(CreateShareRequest request);
    Result<List<FileInfoWithThumbnails>> verify(String linkId, String passwd);
    Result<ShareDetailDTO> getShareDetail(String linkId);
    Result<Boolean> cancelByLinkId(String linkId);
    Result<Integer> countByUserId(Long userId);
} 