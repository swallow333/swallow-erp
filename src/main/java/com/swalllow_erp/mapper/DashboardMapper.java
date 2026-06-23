package com.swalllow_erp.mapper;


import com.swalllow_erp.dto.DashboardStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:44
 */

@Mapper
public interface DashboardMapper {

    // ========== 核心指标 ==========

    /**
     * 统计总商品数
     */
    @Select("SELECT COUNT(*) FROM product WHERE status = 1")
    Integer countTotalProducts();

    /**
     * 统计总订单量（已审核、已发货、已完成）
     */
    @Select("SELECT COUNT(*) FROM sale_order WHERE status IN (1, 2, 3)")
    Integer countTotalOrders();

    /**
     * 统计总销售额
     */
    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM sale_order WHERE status IN (2, 3)")
    BigDecimal sumTotalSales();

    /**
     * 统计总采购额
     */
    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM purchase_order WHERE status IN (1, 2)")
    BigDecimal sumTotalPurchase();

    // ========== 待处理 ==========

    /**
     * 待审核销售订单数（草稿状态）
     */
    @Select("SELECT COUNT(*) FROM sale_order WHERE status = 0")
    Integer countPendingOrders();

    /**
     * 待审核采购订单数（草稿状态）
     */
    @Select("SELECT COUNT(*) FROM purchase_order WHERE status = 0")
    Integer countPendingPurchaseOrders();

    /**
     * 低库存商品数（库存 < 阈值）
     */
    @Select("SELECT COUNT(*) FROM inventory WHERE quantity < #{threshold}")
    Integer countLowStock(@Param("threshold") Integer threshold);

    // ========== 今日统计 ==========

    /**
     * 今日订单数
     */
    @Select("SELECT COUNT(*) FROM sale_order WHERE DATE(create_time) = CURDATE()")
    Integer countTodayOrders();

    /**
     * 今日销售额
     */
    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM sale_order " +
            "WHERE DATE(create_time) = CURDATE() AND status IN (2, 3)")
    BigDecimal sumTodaySales();

    // ========== 趋势数据 ==========

    /**
     * 近 N 天销售趋势
     */
    @Select("SELECT DATE_FORMAT(create_time, '%m-%d') AS date, " +
            "COALESCE(SUM(total_amount), 0) AS amount " +
            "FROM sale_order " +
            "WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY) " +
            "AND status IN (2, 3) " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY DATE(create_time)")
    List<Map<String, Object>> selectSalesTrend(@Param("days") Integer days);

    /**
     * 近 N 天采购趋势
     */
    @Select("SELECT DATE_FORMAT(create_time, '%m-%d') AS date, " +
            "COALESCE(SUM(total_amount), 0) AS amount " +
            "FROM purchase_order " +
            "WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY) " +
            "AND status IN (1, 2) " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY DATE(create_time)")
    List<Map<String, Object>> selectPurchaseTrend(@Param("days") Integer days);

    // ========== TOP排行 ==========

    /**
     * 热销商品排行
     */
    @Select("SELECT p.id AS productId, " +
            "p.title AS productName, " +
            "p.sku AS productSku, " +
            "SUM(d.quantity) AS totalQuantity, " +
            "SUM(d.amount) AS totalAmount " +
            "FROM sale_order_detail d " +
            "JOIN product p ON d.product_id = p.id " +
            "JOIN sale_order o ON d.order_id = o.id " +
            "WHERE o.status IN (2, 3) " +
            "GROUP BY d.product_id " +
            "ORDER BY totalAmount DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> selectTopProducts(@Param("limit") Integer limit);

    // ========== 近期动态 ==========

    /**
     * 近期销售订单动态
     */
    @Select("SELECT order_no AS orderNo, " +
            "customer_name AS customerName, " +
            "total_amount AS amount, " +
            "CASE status " +
            "  WHEN 0 THEN '草稿' " +
            "  WHEN 1 THEN '已审核' " +
            "  WHEN 2 THEN '已发货' " +
            "  WHEN 3 THEN '已完成' " +
            "  WHEN 4 THEN '已取消' " +
            "END AS statusName, " +
            "DATE_FORMAT(create_time, '%Y-%m-%d %H:%i:%s') AS createTime " +
            "FROM sale_order " +
            "ORDER BY create_time DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> selectRecentOrders(@Param("limit") Integer limit);
}