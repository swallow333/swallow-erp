package com.swalllow_erp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.SysOperationLog;
import com.swalllow_erp.dto.request.OperationLogQueryRequest;
import com.github.pagehelper.PageInfo;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:15
 */

public interface SysOperationLogService extends IService<SysOperationLog> {
    /**
     * 记录操作日志
     */
    void log(SysOperationLog log);
    /**
     * 分页查询操作日志
     */
    PageInfo<SysOperationLog> queryPage(OperationLogQueryRequest request);
}