package com.swalllow_erp.config;

import com.swalllow_erp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author: Swallow333
 * @Date: 2026/05/23 20:08
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();

        // 白名单（不需要 Token）
        if (uri.equals("/auth/login") ||
                uri.equals("/auth/refresh") ||
                uri.equals("/doc.html") ||
                uri.startsWith("/webjars") ||
                uri.startsWith("/v2/api-docs")) {
            return true;
        }

        // 获取 Token
        String authHeader = request.getHeader(jwtUtil.getHeader());
        String token = jwtUtil.getTokenFromHeader(authHeader);

        // 验证 Token
        if (token == null || !jwtUtil.validateToken(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或Token已过期\"}");
            return false;
        }

        // 把用户信息存入请求中，方便后续使用
        Integer userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);

        return true;
    }
}