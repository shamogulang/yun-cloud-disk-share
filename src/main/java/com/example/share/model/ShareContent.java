package com.example.share.model;

import lombok.Data;

@Data
public class ShareContent {
    private Long id;
    private Long shareId;
    private Long objId;
    private String objType; // file/dir/tag
} 