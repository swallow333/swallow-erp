package com.swalllow_erp.controller;


import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.DashboardStatistics;
import com.swalllow_erp.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:40
 */

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取仪表盘数据
     */
    @GetMapping("/statistics")
    public CommonResult<DashboardStatistics> getStatistics() {
        DashboardStatistics stats = dashboardService.getStatistics();
        return CommonResult.success(stats);
    }
}