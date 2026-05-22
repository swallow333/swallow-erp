package com.swalllow_erp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    // 自定义方法：根据用户名查询用户
    SysUser getByUsername(String username);
}