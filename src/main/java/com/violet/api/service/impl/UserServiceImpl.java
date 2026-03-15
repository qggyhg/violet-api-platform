package com.violet.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.violet.api.common.Result;
import com.violet.api.entity.User;
import com.violet.api.mapper.UserMapper;
import com.violet.api.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired // 🌟 别忘了这个注解，否则会报空指针异常
    private StringRedisTemplate redisTemplate;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 登录逻辑：统一返回 Result 包装类
     */
    @Override
    public Result<Map<String, Object>> login(User loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginUser.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);

        if (dbUser == null) {
            return Result.error(400, "用户不存在");
        }

        if (encoder.matches(loginUser.getPassword(), dbUser.getPassword())) {
            // 生成 Token
            String token = Jwts.builder()
                    .setSubject(dbUser.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                    .signWith(SignatureAlgorithm.HS256, "mySecretKey2222")
                    .compact();

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return Result.success(data); // 🌟 使用统一 Result 返回
        } else {
            return Result.error(400, "密码错误");
        }
    }

    /**
     * 获取随机名言：加入 Redis 缓存逻辑
     */
    @Override
    public Result<Map<String, Object>> getRandomQuote(String username) {
        // ========== 🌟 新增：Redis 接口防刷限流逻辑 ==========
        String limitKey = "rate_limit:quote:" + username;

        // increment 方法：每次访问自动加 1。如果键不存在，Redis 会自动创建并设为 1
        Long currentCount = redisTemplate.opsForValue().increment(limitKey);

        if (currentCount != null && currentCount == 1) {
            // 如果是周期内的第一次访问，设置这个计数器的寿命为 60 秒
            redisTemplate.expire(limitKey, 60, TimeUnit.SECONDS);
        }

        if (currentCount != null && currentCount > 5) {
            // 如果 60 秒内访问超过 5 次，直接拦截，连缓存都不让进！
            return Result.error(429, "警告：访问过于频繁！每分钟限流 5 次，请稍候再试。");
        }
        // ======================================================
        // 1. 定义 Redis Key
        String redisKey = "quote:" + username;

        // 2. 尝试从 Redis 拿数据
        String cachedQuote = redisTemplate.opsForValue().get(redisKey);
        if (cachedQuote != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("quote", cachedQuote);
            data.put("source", "来自 Redis 缓存（本次不扣费）");
            return Result.success(data);
        }

        // 3. Redis 没有，查数据库余额
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null || user.getLeftNum() <= 0) {
            return Result.error(403, "调用失败：该用户可用余额不足！");
        }

        // 4. 数据库扣费
        user.setLeftNum(user.getLeftNum() - 1);
        userMapper.updateById(user);

        // 5. 准备返回数据
        String realQuote = "我帅得要命！";

        // 🌟 存入 Redis，设置 60 秒有效期
        redisTemplate.opsForValue().set(redisKey, realQuote, 60, TimeUnit.SECONDS);

        Map<String, Object> data = new HashMap<>();
        data.put("quote", realQuote);
        data.put("leftNum", user.getLeftNum());
        data.put("source", "来自 数据库（已扣费并存入缓存）");

        return Result.success(data);
    }
}