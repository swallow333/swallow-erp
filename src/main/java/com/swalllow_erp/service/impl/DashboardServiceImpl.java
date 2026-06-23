package com.swalllow_erp.service.impl;

import com.swalllow_erp.dto.DashboardStatistics;
import com.swalllow_erp.mapper.*;
import com.swalllow_erp.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:38
 */
@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private DashboardMapper dashboardMapper;

    @Override
    public DashboardStatistics getStatistics() {
        DashboardStatistics stats = new DashboardStatistics();
        // 1. 核心指标
        stats.setTotalProducts(dashboardMapper.countTotalProducts());
        stats.setTotalOrders(dashboardMapper.countTotalOrders());
        BigDecimal totalSales = dashboardMapper.sumTotalSales();
        stats.setTotalSales(totalSales != null ? totalSales : BigDecimal.ZERO);
        BigDecimal totalPurchase = dashboardMapper.sumTotalPurchase();
        stats.setTotalPurchase(totalPurchase != null ? totalPurchase : BigDecimal.ZERO);
        // 2. 待处理
        stats.setPendingOrders(dashboardMapper.countPendingOrders());
        stats.setPendingPurchaseOrders(dashboardMapper.countPendingPurchaseOrders());
        stats.setLowStockCount(dashboardMapper.countLowStock(10));
        // 3. 今日统计
        stats.setTodayOrders(dashboardMapper.countTodayOrders());
        BigDecimal todaySales = dashboardMapper.sumTodaySales();
        stats.setTodaySales(todaySales != null ? todaySales : BigDecimal.ZERO);
        // 4. 趋势数据
        List<Map<String, Object>> salesTrendRaw = dashboardMapper.selectSalesTrend(7);
        stats.setSalesTrend(convertToSalesTrend(salesTrendRaw));
        // 5. 热销商品 TOP 5
        List<Map<String, Object>> topProductsRaw = dashboardMapper.selectTopProducts(5);
        stats.setTopProducts(convertToProductRank(topProductsRaw));
        // 6. 近期动态
        List<Map<String, Object>> recentOrdersRaw = dashboardMapper.selectRecentOrders(5);
        stats.setRecentOrders(convertToOrderActivity(recentOrdersRaw));
        return stats;
    }

    // ========== 接口中声明的其他方法 ==========
    @Override
    public List<DashboardStatistics.SalesTrend> getSalesTrend(Integer days) {
        List<Map<String, Object>> salesTrendRaw = dashboardMapper.selectSalesTrend(days);
        return convertToSalesTrend(salesTrendRaw);
    }

    @Override
    public List<DashboardStatistics.ProductRank> getTopProducts(Integer limit) {
        List<Map<String, Object>> topProductsRaw = dashboardMapper.selectTopProducts(limit);
        return convertToProductRank(topProductsRaw);
    }

    @Override
    public List<DashboardStatistics.OrderActivity> getRecentOrders(Integer limit) {
        List<Map<String, Object>> recentOrdersRaw = dashboardMapper.selectRecentOrders(limit);
        return convertToOrderActivity(recentOrdersRaw);
    }

    // ========== 转换方法 ==========
    private List<DashboardStatistics.SalesTrend> convertToSalesTrend(List<Map<String, Object>> raw) {
        List<DashboardStatistics.SalesTrend> result = new ArrayList<>();
        for (Map<String, Object> item : raw) {
            DashboardStatistics.SalesTrend trend = new DashboardStatistics.SalesTrend();
            trend.setDate((String) item.get("date"));
            trend.setAmount((BigDecimal) item.get("amount"));
            result.add(trend);
        }
        return result;
    }

    private List<DashboardStatistics.ProductRank> convertToProductRank(List<Map<String, Object>> raw) {
        List<DashboardStatistics.ProductRank> result = new ArrayList<>();
        for (Map<String, Object> item : raw) {
            DashboardStatistics.ProductRank rank = new DashboardStatistics.ProductRank();
            rank.setProductId((Integer) item.get("productId"));
            rank.setProductName((String) item.get("productName"));
            rank.setProductSku((String) item.get("productSku"));
            rank.setTotalQuantity(((Number) item.get("totalQuantity")).intValue());
            rank.setTotalAmount((BigDecimal) item.get("totalAmount"));
            result.add(rank);
        }
        return result;
    }

    private List<DashboardStatistics.OrderActivity> convertToOrderActivity(List<Map<String, Object>> raw) {
        List<DashboardStatistics.OrderActivity> result = new ArrayList<>();
        for (Map<String, Object> item : raw) {
            DashboardStatistics.OrderActivity activity = new DashboardStatistics.OrderActivity();
            activity.setOrderNo((String) item.get("orderNo"));
            activity.setCustomerName((String) item.get("customerName"));
            activity.setAmount((BigDecimal) item.get("amount"));
            activity.setStatusName((String) item.get("statusName"));
            activity.setCreateTime((String) item.get("createTime"));
            result.add(activity);
        }
        return result;
    }
}