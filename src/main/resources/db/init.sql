CREATE DATABASE IF NOT EXISTS personal_cloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE personal_cloud;

CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `email` VARCHAR(100) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '手机号',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `file_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
    `file_type` VARCHAR(50) COMMENT '文件类型',
    `file_size` BIGINT NOT NULL COMMENT '文件大小(字节)',
    `file_hash` VARCHAR(32) NOT NULL COMMENT '文件hash',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-删除，1-正常',
    `is_system` TINYINT NOT NULL DEFAULT 0 COMMENT '是否为系统文件 0-否，1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_file_hash` (`file_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件信息表';

CREATE TABLE IF NOT EXISTS `share` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `share_url` VARCHAR(255) NOT NULL COMMENT '分享链接',
    `extract_code` VARCHAR(10) COMMENT '提取码',
    `expire_time` DATETIME COMMENT '过期时间',
    `user_id` BIGINT NOT NULL COMMENT '分享人ID',
    `encrypt` TINYINT NOT NULL DEFAULT 0 COMMENT '是否加密 0-否 1-是',
    `pub_type` TINYINT NOT NULL DEFAULT 1 COMMENT '公开类型 1-所有人 2-好友',
    `period` INT COMMENT '有效期长度',
    `period_unit` TINYINT NOT NULL DEFAULT 1 COMMENT '有效期单位 1-天',
    `passwd` VARCHAR(20) COMMENT '分享密码（明文）',
    `link_id` VARCHAR(64) NOT NULL COMMENT '外链ID',
    `link_url` VARCHAR(255) NOT NULL COMMENT '外链URL',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-失效，1-有效',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_link_id` (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件/目录/标签分享主表';

-- 分享内容关联表
CREATE TABLE IF NOT EXISTS `share_content` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `share_id` BIGINT NOT NULL COMMENT '分享ID',
    `obj_id` BIGINT NOT NULL COMMENT '内容/目录/标签ID',
    `obj_type` VARCHAR(10) NOT NULL COMMENT '类型 file/dir/tag',
    PRIMARY KEY (`id`),
    KEY `idx_share_id` (`share_id`),
    KEY `idx_obj_id` (`obj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分享内容关联表'; 