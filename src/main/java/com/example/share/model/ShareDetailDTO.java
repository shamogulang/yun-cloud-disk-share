package com.example.share.model;

import lombok.Data;
import java.util.Date;

@Data
public class ShareDetailDTO {
    private String linkId;
    private String linkUrl;
    private Long userId;
    private String username;
    private Date createTime;
    private Date expireTime;
    private Integer pubType;
    private Integer encrypt;
} 