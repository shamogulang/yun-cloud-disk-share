package com.example.share.model;

import lombok.Data;
import java.util.Date;

@Data
public class Share {
    private Long id;
    private String shareUrl;
    private String extractCode;
    private Date expireTime;
    private Long userId;
    private Date createTime;
    private Integer status;
    private Integer encrypt; // 是否加密 0-否 1-是
    private Integer pubType; // 公开类型 1-所有人 2-好友
    private Integer period; // 有效期长度
    private Integer periodUnit; // 有效期单位 1-天
    private String passwd; // 分享密码（明文）
    private String linkId; // 外链ID
    private String linkUrl; // 外链URL
} 