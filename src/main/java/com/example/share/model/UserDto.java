package com.example.share.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;

    private String username;

    private String email;

    private String phone;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 