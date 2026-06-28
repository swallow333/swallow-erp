package com.swalllow_erp.controller;

import com.github.pagehelper.PageInfo;
import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.request.UserQueryRequest;
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

@RestController // 表明这个类是一个控制器（Controller），并且它的所有方法返回的数据都会直接写入HTTP响应体（Response Body）中，而不是跳转到某个视图页面。
@RequestMapping("/users")  // 将一个特定请求或者请求模式映射到一个控制器之上，表示类中的所有响应请求的方法都是以该地址作为父路径
public class SysUserController {
    @Autowired  // 字段注入:Spring自动注入SysUserService，不用手动new，不推荐，破坏封装性，难以进行单元测试
    private SysUserService sysUserService;
    // GET /users - 查询所有
    @GetMapping // 使用@GetMapping注解映射HTTP GET请求到父路径到CommonResult方法
    public CommonResult<List<SysUser>> list() {
        List<SysUser> list = sysUserService.list();
        list.forEach(u -> u.setPassword(null));
        return CommonResult.success(list);
    }
    // GET /users/{id} - 查询单个
    @GetMapping("/{id}")
    public CommonResult<SysUser> getById(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
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
        SysUser existUser = sysUserService.getByUsername(user.getUsername());
        if (existUser != null) {
            return CommonResult.error(CommonCodeEnum.USERNAME_EXIST);
        }
        // 密码加密
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        sysUserService.save(user);
        user.setPassword(null);
        return CommonResult.created(user);  // 返回 201
    }
    // PUT /users/{id} - 全量更新
    @PutMapping("/{id}")
    public CommonResult<Void> update(@PathVariable Integer id, @RequestBody SysUser user) {
        SysUser existUser = sysUserService.getById(id);
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
        sysUserService.updateById(user);
        return CommonResult.success();
    }
    // DELETE /users/{id} - 删除用户
    @DeleteMapping("/{id}")
    public CommonResult<Void> delete(@PathVariable Long id) {
        SysUser existUser = sysUserService.getById(id);
        if (existUser == null) {
            return CommonResult.error(CommonCodeEnum.NOT_FOUND);
        }
        sysUserService.removeById(id);

        return CommonResult.noContent(CommonCodeEnum.NO_CONTENT.getMessage(), null);  // 返回 204
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/{id}/status")
    public CommonResult<Void> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        SysUser existUser = sysUserService.getById(id);
        if (existUser == null) {
            return CommonResult.error(CommonCodeEnum.USER_NOT_EXIST);
        }
        existUser.setStatus(status);
        sysUserService.updateById(existUser);
        return CommonResult.success(status == 1 ? "启用成功" : "禁用成功", null);
    }

    @PostMapping("/page")
    public CommonResult<PageInfo<SysUser>> queryPage(@RequestBody UserQueryRequest request) {
        PageInfo<SysUser> page = sysUserService.queryPage(request);
        return CommonResult.success(page);
    }
}