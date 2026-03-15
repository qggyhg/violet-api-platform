package com.violet.api.config;

import com.violet.api.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;



// 这个注解告诉 Spring：请派一个“专员”，盯着所有的 Controller 和 拦截器
@RestControllerAdvice
public class GlobalExceptionHandler{

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    // 只要系统里抛出了 Exception 异常，都会被这个方法抓住
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {

        if (e.getMessage() != null && e.getMessage().contains("Token")) {
            return Result.error(401, e.getMessage());
        } else {
            return Result.error(500, "服务器炸了！" + e.getMessage());
        }


    }
}