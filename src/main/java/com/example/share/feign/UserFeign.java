package com.example.share.feign;

import com.example.share.feign.config.FeignConfiguration;
import com.example.share.model.AuthRequest;
import com.example.share.model.Result;
import com.example.share.model.UserAuthData;
import com.example.share.model.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user.auth:''}", configuration = FeignConfiguration.class)
public interface UserFeign {


    @PostMapping("/user/auth")
    Result<UserAuthData> auth(@RequestBody AuthRequest authRequest);

    @GetMapping("/user/{userId}")
    Result<UserDto> getUserInfo(@PathVariable Long userId);

}