package com.example.share.config;

import com.example.share.feign.UserFeign;
import com.example.share.model.AuthRequest;
import com.example.share.model.Result;
import com.example.share.model.UserAuthData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    @Lazy
    private UserFeign userFeign;
    @Autowired
    private  AuthProperties authProperties;
    @Autowired
    private ThirdAuthHelper helper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            String serviceName = request.getHeader("X-Service-Name");
            String timestamp = request.getHeader("X-Timestamp");
            String signature = request.getHeader("X-Signature");
            if(StringUtils.hasText(serviceName) && StringUtils.hasText(timestamp)  && StringUtils.hasText(signature) ){
                boolean verify = helper.verify(serviceName, timestamp, signature);
                if(!verify){
                    thirdAuthWriteAuthFail(response);
                    return false;
                }
                return true;
            }
            writeAuthFail(response);
            return false;
        }
        AuthRequest authRequest =new AuthRequest();
        authRequest.setToken(token);
        Result<UserAuthData> userAuthDataBaseResult = userFeign.auth(authRequest);
        UserAuthData data = userAuthDataBaseResult.getData();
        if (data == null || !(Boolean.TRUE.equals(data.getValid()))) {
            writeAuthFail(response);
            return false;
        }
        request.setAttribute("userId", data.getUserId());
        return true;
    }

    private void writeAuthFail(HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":100000,\"msg\":\"未授权或token无效\",\"data\":null}");
        writer.flush();
        writer.close();
    }

    private void thirdAuthWriteAuthFail(HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":100001,\"msg\":\"三方鉴权失败\",\"data\":null}");
        writer.flush();
        writer.close();
    }
} 