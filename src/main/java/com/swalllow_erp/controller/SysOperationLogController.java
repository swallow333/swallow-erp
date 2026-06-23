package com.swalllow_erp.controller;


import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.request.OperationLogQueryRequest;
import com.swalllow_erp.entity.SysOperationLog;
import com.swalllow_erp.service.SysOperationLogService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:18
 */

@RestController
@RequestMapping("/logs")
public class SysOperationLogController {

    @Autowired
    private SysOperationLogService logService;

    @PostMapping("/page")
    public CommonResult<PageInfo<SysOperationLog>> queryPage(@RequestBody OperationLogQueryRequest request) {
        PageInfo<SysOperationLog> page = logService.queryPage(request);
        return CommonResult.success(page);
    }
}