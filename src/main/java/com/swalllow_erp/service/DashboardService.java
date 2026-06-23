package com.swalllow_erp.service;

import com.swalllow_erp.dto.DashboardStatistics;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:37
 */
public interface DashboardService {
    /**
     * 获取仪表盘统计数据
     */
    DashboardStatistics getStatistics();

    /**
     * 获取近7天销售趋势
     */
    List<DashboardStatistics.SalesTrend> getSalesTrend(Integer days);

    /**
     * 获取热销商品排行
     */
    List<DashboardStatistics.ProductRank> getTopProducts(Integer limit);

    /**
     * 获取近期订单动态
     */
    List<DashboardStatistics.OrderActivity> getRecentOrders(Integer limit);
}