package com.violet.api.config;

import com.violet.api.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 告诉 Spring 这是一个配置类，启动时要加载它
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**") // 保安拦截所有 /api/ 开头的请求
                .excludePathPatterns("/login", "/register"); // 登录和注册绝对不能拦，否则死循环了
    }
}