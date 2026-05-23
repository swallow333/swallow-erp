package com.swalllow_erp.controller;

import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.entity.SysUser;
import com.swalllow_erp.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 登录拦截器
 * @Author: Swallow333
 * @Date: 2026/05/22 18:11
 */

@RestController //  表明这个类是一个控制器（Controller），并且它的所有方法返回的数据都会直接写入HTTP响应体（Response Body）中，而不是跳转到某个视图页面。
@RequestMapping("/auth")    // 将一个特定请求或者请求模式映射到一个控制器之上，表示类中的所有响应请求的方法都是以该地址作为父路径
public class AuthController {

    @PostMapping("/login")
    public CommonResult<LoginResponse> login(@RequestBody LoginParam param) {
        // 验证通过后，返回 Token
        String token = generateToken(user);
        LoginResponse response = new LoginResponse(token, user);
        return CommonResult.success("登录成功", response);
    }

    @PostMapping("/logout")
    public CommonResult<Void> logout(@RequestHeader("Authorization") String token) {
        // 清除 Token
        return CommonResult.noContent();  // 204
    }

    @GetMapping("/me")
    public CommonResult<SysUser> getCurrentUser(@RequestHeader("Authorization") String token) {
        SysUser user = getUserFromToken(token);
        return CommonResult.success(user);
    }
}