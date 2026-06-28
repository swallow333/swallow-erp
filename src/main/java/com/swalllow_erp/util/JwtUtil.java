package com.swalllow_erp.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

import lombok.Getter;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



/**
 * JWT 工具类
 *
 * @Author: Swallow333
 * @Date: 2026/05/23 19:35
 */
@Component  // 让 Spring 自动检测并管理该类的实例
public class JwtUtil {
    // 注入获取对应属性文件中定义的属性值, : 表示默认值
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration:7200000}")
    private Long expiration;  // 2小时 = 7200000 毫秒
    @Getter
    @Value("${jwt.header:Authorization}")
    private String header;
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    // 生成 Token
    public String generateToken(Integer userId, String username) {
        Map<String, Object> claims = new HashMap<>();   // Claims是JWT的有效载荷(Payload)部分，包含了用于验证和识别令牌持有者的关键信息
        Date now = new Date();  // 设置Token过期时间
        Date expireDate = new Date(now.getTime() + expiration);
        claims.put("sub", userId);          // 主题
        claims.put("username", username);   // 用户名
        claims.put("iat", now);             // 签发时间
        claims.put("exp", expireDate);      // 过期时间
        System.out.println("生成token:" + JWTUtil.createToken(claims, secret.getBytes(StandardCharsets.UTF_8)));
        return JWTUtil.createToken(claims, secret.getBytes(StandardCharsets.UTF_8));
    }

    // 验证 Token 是否有效
    public Boolean validateToken(String token) {
        try {
            if (!JWTUtil.verify(token, secret.getBytes(StandardCharsets.UTF_8))) {
                return false;
            }
            JWTValidator.of(token).validateDate(new Date());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 从Token中获取用户ID
    public Integer getUserIdFromToken(String token) {
        try {
            if (token != null && token.startsWith(tokenPrefix)) {
                token = token.substring(tokenPrefix.length());
            }
            JWT jwt = JWTUtil.parseToken(token);
            jwt.setKey(secret.getBytes(StandardCharsets.UTF_8));
            Object userIdObj = jwt.getPayload("userId");
            if (userIdObj instanceof Integer) {
                return (Integer) userIdObj;
            } else if (userIdObj instanceof String) {
                return Integer.parseInt((String) userIdObj);
            } else if (userIdObj instanceof Number) {
                return ((Number) userIdObj).intValue();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // 从Token中获取用户名
    public String getUsernameFromToken(String token) {
        try {
            if (token != null && token.startsWith(tokenPrefix)) {
                token = token.substring(tokenPrefix.length());
            }
            JWT jwt = JWTUtil.parseToken(token);
            jwt.setKey(secret.getBytes(StandardCharsets.UTF_8));
            Object usernameObj = jwt.getPayload("username");
            return usernameObj != null ? usernameObj.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    // 刷新Token
    public String refreshToken(String token) {
        Integer userId = getUserIdFromToken(token);
        String username = getUsernameFromToken(token);
        if (userId == null || username == null) {
            return null;
        }
        return generateToken(userId, username);
    }

    // 从请求头获取 Token
    public String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(tokenPrefix)) {
            return authHeader.substring(tokenPrefix.length()).trim();
        }
        return null;
    }
}