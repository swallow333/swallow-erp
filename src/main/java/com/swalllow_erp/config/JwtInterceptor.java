package com.swalllow_erp.config;

import com.swalllow_erp.service.SysMenuService;
import com.swalllow_erp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Author: Swallow333
 * @Date: 2026/05/23 20:08
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String uri = request.getRequestURI();
        // 1. 白名单
        if (uri.contains("/auth/login") ||
                uri.contains("/auth/refresh") ||
                uri.contains("/doc.html") ||
                uri.contains("/swagger")) {
            return true;
        }
        // 2. 验证 Token
        String authHeader = request.getHeader(jwtUtil.getHeader());
        String token = jwtUtil.getTokenFromHeader(authHeader);
        if (token == null || !jwtUtil.validateToken(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"请先登录\"}");
            return false;
        }
        // 3. 获取用户ID
        Integer userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        // 4. 权限验证（可选）
        // 从请求中提取权限标识（如 @PreAuthorize 或自定义）
        String method = request.getMethod();
        // ... 权限校验逻辑
        return true;
    }
}