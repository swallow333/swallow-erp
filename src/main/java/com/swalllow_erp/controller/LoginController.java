package com.swalllow_erp.controller;

import com.swalllow_erp.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Swallow333
 * @Date: 2026/05/22 18:11
 */
@RestController
@RequestMapping("/system")
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     *
     * @param params 请求参数 {username: "admin", password: "123456"}
     * @return 登录结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody Map<String, String> params) {
        // 1. 获取参数
        String username = params.get("username");
        String password = params.get("password");

        // 2. 参数校验
        if (username == null || username.isEmpty()) {
            return AjaxResult.error("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return AjaxResult.error("密码不能为空");
        }

        // 3. 查询用户
        SysUser user = sysUserService.getByUsername(username);

        // 4. 用户不存在
        if (user == null) {
            return AjaxResult.error("用户名不存在");
        }

        // 5. 密码校验（MD5加密后对比）
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(user.getPassword())) {
            return AjaxResult.error("密码错误");
        }

        // 6. 账号状态校验（1正常 0禁用）
        if (user.getStatus() == 0) {
            return AjaxResult.error("账号已被禁用，请联系管理员");
        }

        // 7. 登录成功，密码不返回前端
        user.setPassword(null);
        return AjaxResult.success("登录成功", user);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public AjaxResult logout() {
        // TODO: 清除Session或Token
        return AjaxResult.success("退出成功");
    }
}