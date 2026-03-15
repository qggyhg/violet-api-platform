package com.violet.api.interceptor;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头里拿到 Token
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("无Token，请重新登录"); // 暂时先抛异常，下一关我们做全局异常处理来美化它
        }

        try {
            // 2. 验证 Token
            String username = Jwts.parser()
                    .setSigningKey("mySecretKey2222") // 必须和发卡时的密钥一致
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            // 3. 验证通过！把用户名贴在请求(request)上，传给后面的 Controller
            request.setAttribute("username", username);
            return true; // return true 代表保安放行！

        } catch (Exception e) {
            throw new RuntimeException("Token无效或已过期");
        }
    }
}