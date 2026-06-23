package com.swalllow_erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swalllow_erp.entity.SysOperationLog;
import com.swalllow_erp.mapper.SysOperationLogMapper;
import com.swalllow_erp.service.SysOperationLogService;
import com.swalllow_erp.dto.request.OperationLogQueryRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:16
 */

@Service
public class SysOperationLogServiceImpl extends ServiceImpl<SysOperationLogMapper, SysOperationLog>
        implements SysOperationLogService {

    @Async
    @Override
    public void log(SysOperationLog log) {
        save(log);
    }

    @Override
    public PageInfo<SysOperationLog> queryPage(OperationLogQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<SysOperationLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getUsername())) {
            wrapper.like(SysOperationLog::getUsername, request.getUsername());
        }
        if (StringUtils.hasText(request.getModule())) {
            wrapper.eq(SysOperationLog::getModule, request.getModule());
        }
        if (StringUtils.hasText(request.getOperationType())) {
            wrapper.eq(SysOperationLog::getOperationType, request.getOperationType());
        }
        if (request.getStatus() != null) {
            wrapper.eq(SysOperationLog::getStatus, request.getStatus());
        }

        wrapper.orderByDesc(SysOperationLog::getCreateTime);
        List<SysOperationLog> list = list(wrapper);
        return new PageInfo<>(list);
    }
}