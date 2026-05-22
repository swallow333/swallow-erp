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

@RestController
@RequestMapping("/system")
public class LoginController {
    @Autowired
    private SysUserService sysUserService;
    @PostMapping("/login")
    public CommonResult<SysUser> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
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
        // 登录成功，密码不返回前端
        user.setPassword(null);
        return CommonResult.success("登录成功", user);
    }
}