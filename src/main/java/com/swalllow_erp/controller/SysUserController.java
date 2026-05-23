package com.swalllow_erp.controller;

import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.entity.SysUser;
import com.swalllow_erp.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/05/23 16:50
 */

@RestController
@RequestMapping("/users")  // ← 复数
public class SysUserController {
    @Autowired
    private SysUserService userService;
    // GET /users - 查询所有
    @GetMapping
    public CommonResult<List<SysUser>> list() {
        List<SysUser> list = userService.list();
        list.forEach(u -> u.setPassword(null));
        return CommonResult.success(list);
    }
    // GET /users/{id} - 查询单个
    @GetMapping("/{id}")
    public CommonResult<SysUser> getById(@PathVariable Long id) {
        SysUser user = userService.getById(id);
        if (user == null) {
            return CommonResult.error(CommonCodeEnum.NOT_FOUND);
        }
        user.setPassword(null);
        return CommonResult.success(user);
    }
    // POST /users - 创建用户
    @PostMapping
    public CommonResult<SysUser> create(@RequestBody SysUser user) {
        // 检查用户名是否存在
        SysUser existUser = userService.getByUsername(user.getUsername());
        if (existUser != null) {
            return CommonResult.error(CommonCodeEnum.USERNAME_EXIST);
        }
        // 密码加密
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        userService.save(user);
        user.setPassword(null);
        return CommonResult.created(user);  // 返回 201
    }
    // PUT /users/{id} - 全量更新
    @PutMapping("/{id}")
    public CommonResult<Void> update(@PathVariable Integer id, @RequestBody SysUser user) {
        SysUser existUser = userService.getById(id);
        if (existUser == null) {
            return CommonResult.error(CommonCodeEnum.NOT_FOUND);
        }
        user.setId(id);
        // 如果密码不为空，加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(md5Password);
        } else {
            user.setPassword(null);
        }
        userService.updateById(user);
        return CommonResult.success();
    }
    // DELETE /users/{id} - 删除用户
    @DeleteMapping("/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        SysUser existUser = userService.getById(id);
        if (existUser == null) {
            return CommonResult.error(CommonCodeEnum.NOT_FOUND);
        }
        userService.removeById(id);

        return CommonResult.noContent(CommonCodeEnum.NO_CONTENT.getMessage(), null);  // 返回 204
    }
}