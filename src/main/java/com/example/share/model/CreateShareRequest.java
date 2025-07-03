package com.example.share.model;

import lombok.Data;
import java.util.List;

@Data
public class CreateShareRequest {
    private Integer encrypt; // 是否加密 0-否 1-是
    private List<Long> coIDLst; // 文件ID列表
    private List<Long> caIDLst; // 目录ID列表
    private Integer pubType; // 公开类型 1-所有人 2-好友
    private Integer period; // 有效期长度
    private Integer periodUnit; // 有效期单位 1-天
    private Long userId; // 分享人ID
}