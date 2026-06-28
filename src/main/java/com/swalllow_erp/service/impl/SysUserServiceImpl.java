package com.swalllow_erp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swalllow_erp.dto.request.UserQueryRequest;
import com.swalllow_erp.entity.SysUser;
import com.swalllow_erp.mapper.SysUserMapper;
import com.swalllow_erp.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getByUsername(String username) {
        // 只根据用户名查询单个用户
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return this.getOne(wrapper);
    }

    @Override
    public PageInfo<SysUser> queryPage(UserQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（用户名或昵称）
        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.and(w -> w
                    .like(SysUser::getUsername, request.getKeyword())
                    .or()
                    .like(SysUser::getNickname, request.getKeyword())
            );
        }

        // 状态筛选
        if (request.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, request.getStatus());
        }

        wrapper.orderByDesc(SysUser::getCreateTime);

        List<SysUser> list = list(wrapper);
        return new PageInfo<>(list);
    }
}