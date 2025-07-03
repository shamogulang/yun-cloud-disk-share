package com.example.share.feign;

import com.example.share.model.AuthRequest;
import com.example.share.model.Result;
import com.example.share.model.UserAuthData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserFeign {


    @PostMapping("/user/auth")
    Result<UserAuthData> auth(@RequestBody AuthRequest authRequest);

}