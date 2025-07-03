package com.example.share.model;

import lombok.Data;
import java.util.Date;

@Data
public class FileInfoDTO {
    private Long id;
    private String fileName;
    private Long fileSize;
    private Date updateTime;
    private String type; // 文件类型
} 