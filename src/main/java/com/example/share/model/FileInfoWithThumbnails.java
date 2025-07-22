package com.example.share.model;

import lombok.Data;

import java.util.List;

@Data
public class FileInfoWithThumbnails extends FileInfo {
    private List<ThumbnailUrl> thumbnailUrls;
} 