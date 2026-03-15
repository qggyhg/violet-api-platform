package com.violet.api;
/**
 * @Author: violet
 * @Date: 2026/3/13 15:54
 * @ProjectName: Violet_Job
 */
import com.baomidou.mybatisplus.core.toolkit.support.BiIntFunction;
import com.violet.api.common.Result;
import com.violet.api.entity.User;
import com.violet.api.mapper.UserMapper;
import com.violet.api.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(tags = "核心业务接口")

@RestController//用来处理合同谈判请求的api
public class HelloController{
    @Autowired
    private UserService userService;


    //    @GetMapping("/hello")//绑定地址：返回hello
//    public Map<Object, Object> sayHello(@RequestParam(defaultValue = "Guest") String name) {
//        Map<Object, Object> result = new HashMap<>();
//        result.put("code", 200);
//        result.put("msg", "请求成功！");
//        result.put("data", "Hello," + name + "!");
//        return result;
//    }
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /*@PostMapping("/register")
    public Map<String, Object> registerUser(@RequestBody User user) {
        //加密
        String encoderPassword = encoder.encode(user.getPassword());
        //加密返回
        user.setPassword(encoderPassword);
        //加密入库
        userMapper.insert(user);
        int rows = userMapper.insert(user);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "注册成功！");
//        result1.put("data", user);
        return result;

    }*/
    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        return userService.login(user);
    }
    @ApiOperation("获取随机名言接口（需要Token扣费）")
    @GetMapping("/api/quote")
    public Result<Map<String, Object>> getQuote(@RequestAttribute("username")String username) {
        // 服务员直接把 Token 交给大厨去处理
        return userService.getRandomQuote(username);
    }

}
