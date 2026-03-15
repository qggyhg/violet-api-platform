package com.violet.api.service;

import com.violet.api.common.Result;
import com.violet.api.entity.User;

import java.util.Map;

/**
 * @Author: violet
 * @Date: 2026/3/14 15:12
 * @ProjectName: Violet_Job
 */
public interface UserService{
    Result<Map<String, Object>> login(User user);

    Result<Map<String, Object>> getRandomQuote(String token);
}
