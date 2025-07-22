package com.example.share.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileInfo {
    private Long id;
    private Long userId;
    private Long parentId;
    private String fileName;
    private Boolean isDirectory;
    private String fileCategory;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean hidden;

    // For files (not directories)
    private Long fileSize;
    private String fileType;
    private String fileHash;
    private String uploadUrl; // S3上传地址，物理文件不存在时返回
} 