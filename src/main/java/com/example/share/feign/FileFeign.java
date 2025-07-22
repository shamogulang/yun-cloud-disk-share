package com.example.share.feign;

import com.example.share.feign.config.FeignConfiguration;
import com.example.share.feign.config.FeignInterceptor;
import com.example.share.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "file-service", url = "${file.url:''}",configuration = FeignConfiguration.class)
public interface FileFeign {


    @PostMapping("/file/listByIds")
    Result<List<FileInfoWithThumbnails>> listByIds(@RequestBody ListByIdsReq list);
}