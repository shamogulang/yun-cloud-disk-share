package com.example.share.model;

import lombok.Data;
import java.util.List;

@Data
public class CreateShareResponse {
    private String objID; // 内容/目录唯一标识
    private String passwd; // 密码（明文）
    private String linkID; // 生成的外链ID
    private String linkUrl; // 生成的外链URL
} 