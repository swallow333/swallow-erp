package com.swalllow_erp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Swallow333
 * @Date: 2026/05/23 19:35
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret:SwallowERPSystemSecretKey2026!@#$%^&*()}")
    private String secret;

    @Value("${jwt.expiration:7200000}")
    private Long expiration;  // 2小时 = 7200000 毫秒

    @Value("${jwt.header:Authorization}")
    private String header;

    @Value("${jwt.tokenPrefix:Bearer }")
    private String tokenPrefix;

    /**
     * 生成 Token
     */
    public String generateToken(Integer userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 从 Token 中获取用户ID
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        return Integer.parseInt(claims.getSubject());
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        return (String) claims.get("username");
    }

    /**
     * 验证 Token 是否有效
     */
    public Boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return false;
            }
            // 检查是否过期
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新 Token
     */
    public String refreshToken(String token) {
        Integer userId = getUserIdFromToken(token);
        String username = getUsernameFromToken(token);
        if (userId == null || username == null) {
            return null;
        }
        return generateToken(userId, username);
    }

    /**
     * 获取 Token 中的 Claims
     */
    private Claims getClaimsFromToken(String token) {
        try {
            // 去掉 Bearer 前缀
            if (token != null && token.startsWith(tokenPrefix)) {
                token = token.substring(tokenPrefix.length());
            }
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取签名密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 从请求头获取 Token
     */
    public String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(tokenPrefix)) {
            return authHeader.substring(tokenPrefix.length());
        }
        return null;
    }

    // ========== Getter ==========
    public String getHeader() {
        return header;
    }
}
