package com.violet.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Author: violet
 * @Date: 2026/3/13 15:43
 * @ProjectName: Violet_Job
 */
@SpringBootApplication
@MapperScan("com.violet.api.mapper")
public class ApiApplication {
    public static void main(String[] args) {
        // 这里是程序的总入口，相当于按下了启动按钮
        SpringApplication.run(ApiApplication.class, args);
    }
}