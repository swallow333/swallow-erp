package com.swalllow_erp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swalllow_erp.dto.request.LoginRequest;
import com.swalllow_erp.entity.SysUser;
import com.swalllow_erp.service.SysUserService;
import com.swalllow_erp.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

/**
 * @Author: Swallow333
 * @Date: 2026/05/24 21:51
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;  // 模拟 HTTP 请求

    @Autowired
    private ObjectMapper objectMapper;  // 对象转 JSON

    @MockitoBean
    private SysUserService sysUserService;  // Mock Service，不查真实数据库

    @MockitoBean
    private JwtUtil jwtUtil;  // Mock JWT 工具

    private SysUser mockUser;

    @BeforeEach
    void setUp() {
        // 准备模拟的用户数据
        mockUser = new SysUser();
        mockUser.setId(1);
        mockUser.setUsername("admin");
        mockUser.setPassword("e10adc3949ba59abbe56e057f20f883e");
        mockUser.setNickname("管理员");
        mockUser.setStatus(1);
    }

    // ========== 登录接口测试 ==========

    @Test
    void testLoginSuccess() throws Exception {
        // 1. 准备请求参数
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");

        // 2. Mock Service 行为
        when(sysUserService.getByUsername("admin")).thenReturn(mockUser);
        when(jwtUtil.generateToken(1, "admin")).thenReturn("mock-token-123");

        // 3. 执行请求并验证
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("登录成功"))
                .andExpect(jsonPath("$.data.token").value("mock-token-123"))
                .andExpect(jsonPath("$.data.user.username").value("admin"));
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("notexist");
        request.setPassword("123456");

        // Mock 用户不存在
        when(sysUserService.getByUsername("notexist")).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(10001))
                .andExpect(jsonPath("$.message").value("用户不存在"));
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrongpassword");

        when(sysUserService.getByUsername("admin")).thenReturn(mockUser);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(10002))
                .andExpect(jsonPath("$.message").value("密码错误"));
    }

    @Test
    void testLoginDisabledUser() throws Exception {
        // 设置用户状态为禁用
        mockUser.setStatus(0);

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");

        when(sysUserService.getByUsername("admin")).thenReturn(mockUser);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(10003))
                .andExpect(jsonPath("$.message").value("账号已被禁用"));
    }

    // ========== 获取当前用户测试 ==========

    @Test
    void testGetCurrentUserSuccess() throws Exception {
        String token = "valid-token";

        when(jwtUtil.getHeader()).thenReturn("Authorization");
        when(jwtUtil.getTokenFromHeader("Bearer " + token)).thenReturn(token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getUserIdFromToken(token)).thenReturn(1);
        when(sysUserService.getById(1)).thenReturn(mockUser);

        mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    void testGetCurrentUserInvalidToken() throws Exception {
        String invalidToken = "invalid-token";

        when(jwtUtil.getHeader()).thenReturn("Authorization");
        when(jwtUtil.getTokenFromHeader("Bearer " + invalidToken)).thenReturn(invalidToken);
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("未登录或登录已过期"));
    }

    @Test
    void testGetCurrentUserNoToken() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("未登录或登录已过期"));
    }

    // ========== 刷新 Token 测试 ==========

    @Test
    void testRefreshTokenSuccess() throws Exception {
        String oldToken = "old-token";
        String newToken = "new-token";

        when(jwtUtil.getHeader()).thenReturn("Authorization");
        when(jwtUtil.getTokenFromHeader("Bearer " + oldToken)).thenReturn(oldToken);
        when(jwtUtil.validateToken(oldToken)).thenReturn(true);
        when(jwtUtil.refreshToken(oldToken)).thenReturn(newToken);
        when(jwtUtil.getUserIdFromToken(oldToken)).thenReturn(1);
        when(sysUserService.getById(1)).thenReturn(mockUser);

        mockMvc.perform(post("/auth/refresh")
                        .header("Authorization", "Bearer " + oldToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").value(newToken));
    }

    // ========== 登出测试 ==========

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("退出成功"));
    }
}