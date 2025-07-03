package com.example.share.model;

import lombok.Data;

@Data
public class ShareVerifyRequest {
    private String linkId;
    private String passwd;
} 