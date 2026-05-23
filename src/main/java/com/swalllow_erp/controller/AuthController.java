package com.swalllow_erp.controller;

import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.request.LoginRequest;
import com.swalllow_erp.dto.response.LoginResponse;
import com.swalllow_erp.entity.SysUser;
import com.swalllow_erp.service.SysUserService;

import com.swalllow_erp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/**
 * 登录控制器
 * @Author: Swallow333
 * @Date: 2026/05/22 18:11
 */
@RestController //  表明这个类是一个控制器（Controller），并且它的所有方法返回的数据都会直接写入HTTP响应体（Response Body）中，而不是跳转到某个视图页面。
@RequestMapping("/auth")    // 将一个特定请求或者请求模式映射到一个控制器之上，表示类中的所有响应请求的方法都是以该地址作为父路径
public class AuthController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 登录
     */
    @PostMapping("/login")
    public CommonResult<LoginResponse> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        // 参数校验
        if (username == null || username.isEmpty()) {
            return CommonResult.error(CommonCodeEnum.PARAM_IS_EMPTY);
        }
        if (password == null || password.isEmpty()) {
            return CommonResult.error(CommonCodeEnum.PARAM_IS_EMPTY);
        }
        // 查询用户
        SysUser user = sysUserService.getByUsername(username);
        if (user == null) {
            return CommonResult.error(CommonCodeEnum.USER_NOT_EXIST);
        }
        // 密码校验
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(user.getPassword())) {
            return CommonResult.error(CommonCodeEnum.USER_PASSWORD_ERROR);
        }
        // 账号状态校验
        if (user.getStatus() == 0) {
            return CommonResult.error(CommonCodeEnum.USER_DISABLED);
        }
        // 生成 JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        // 返回响应
        user.setPassword(null);
        LoginResponse response = new LoginResponse(token, user);
        return CommonResult.success("登录成功", response);
    }
    /**
     * 登出（JWT 无状态，客户端丢弃 Token 即可）
     */
    @PostMapping("/logout")
    public CommonResult<Void> logout() {
        // JWT 是无状态的，服务端不需要存储
        // 如果需要黑名单，可以存入 Redis
        return CommonResult.success("退出成功", null);
    }
    /**
     * 获取当前用户信息（需要 Token）
     */
    @GetMapping("/me")
    public CommonResult<SysUser> getCurrentUser(HttpServletRequest request) {
        // 从请求头获取 Token
        String authHeader = request.getHeader(jwtUtil.getHeader());
        String token = jwtUtil.getTokenFromHeader(authHeader);
        if (token == null) {
            return CommonResult.error(CommonCodeEnum.UNAUTHORIZED);
        }
        // 验证 Token
        if (!jwtUtil.validateToken(token)) {
            return CommonResult.error(CommonCodeEnum.UNAUTHORIZED);
        }
        // 获取用户信息
        Integer userId = jwtUtil.getUserIdFromToken(token);
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            return CommonResult.error(CommonCodeEnum.USER_NOT_EXIST);
        }
        user.setPassword(null);
        return CommonResult.success(user);
    }
    /**
     * 刷新 Token
     */
    @PostMapping("/refresh")
    public CommonResult<LoginResponse> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(jwtUtil.getHeader());
        String token = jwtUtil.getTokenFromHeader(authHeader);
        if (token == null) {
            return CommonResult.error(CommonCodeEnum.UNAUTHORIZED);
        }
        if (!jwtUtil.validateToken(token)) {
            return CommonResult.error(CommonCodeEnum.UNAUTHORIZED);
        }
        // 刷新 Token
        String newToken = jwtUtil.refreshToken(token);
        if (newToken == null) {
            return CommonResult.error(CommonCodeEnum.UNAUTHORIZED);
        }
        // 获取用户信息
        Integer userId = jwtUtil.getUserIdFromToken(newToken);
        SysUser user = sysUserService.getById(userId);
        user.setPassword(null);
        LoginResponse response = new LoginResponse(newToken, user);
        return CommonResult.success("刷新成功", response);
    }
}