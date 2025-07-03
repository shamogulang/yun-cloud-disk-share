package com.example.share.model;

import lombok.Data;

@Data
public  class UserAuthData {
        private Boolean valid;
        private Long userId;
        private String username;
        private String message;
}