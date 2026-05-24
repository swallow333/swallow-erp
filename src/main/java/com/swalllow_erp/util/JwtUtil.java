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
 * JWT工具类
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
    // 生成Token
    public String generateToken(Integer userId, String username) {
        Map<String, Object> claims = new HashMap<>();   // Claims是JWT的有效载荷(Payload)部分，包含了用于验证和识别令牌持有者的关键信息
        claims.put("userId", userId);
        claims.put("username", username);
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);
        return Jwts.builder()   // 构建器初始化
                .claims(claims)  // 自定义声明设置
                .subject(String.valueOf(userId))    // 通常用于标识令牌的拥有者,通常是用户唯一标识符
                .issuedAt(now)  // 签发时间设置
                .expiration(expireDate) // 过期时间设置
                .signWith(getSecretKey())   //  使用指定密钥对 JWT 进行签名，确保令牌的完整性和真实性
                .compact(); // 生成最终令牌
    }
    // 从Token中获取用户ID
    public Integer getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        return Integer.parseInt(claims.getSubject());
    }
     // 从Token中获取用户名
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }
        return (String) claims.get("username");
    }
    // 验证Token是否有效
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
    // 刷新Token
    public String refreshToken(String token) {
        Integer userId = getUserIdFromToken(token);
        String username = getUsernameFromToken(token);
        if (userId == null || username == null) {
            return null;
        }
        return generateToken(userId, username);
    }
    // 获取Token中的Claims
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
    // 获取签名密钥
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // 签名算法选择
    }
    // 从请求头获取Token
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
