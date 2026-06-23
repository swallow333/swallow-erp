package com.swalllow_erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.StockIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 2:19
 */

@Mapper
public interface StockInMapper extends BaseMapper<StockIn> {

    /**
     * 根据入库单号查询
     */
    @Select("SELECT * FROM stock_in WHERE in_no = #{inNo}")
    StockIn findByInNo(@Param("inNo") String inNo);

    /**
     * 查询入库单列表（关联供应商名称、采购订单号）
     */
    @Select("SELECT si.*, " +
            "s.name AS supplierName, " +
            "po.order_no AS purchaseOrderNo " +
            "FROM stock_in si " +
            "LEFT JOIN supplier s ON si.supplier_id = s.id " +
            "LEFT JOIN purchase_order po ON si.purchase_order_id = po.id " +
            "ORDER BY si.create_time DESC")
    List<StockIn> selectListWithInfo();

    /**
     * 根据采购订单ID查询入库单
     */
    @Select("SELECT * FROM stock_in WHERE purchase_order_id = #{purchaseOrderId}")
    List<StockIn> selectByPurchaseOrderId(@Param("purchaseOrderId") Integer purchaseOrderId);

    /**
     * 统计某供应商的入库总数量
     */
    @Select("SELECT COALESCE(SUM(total_quantity), 0) FROM stock_in WHERE supplier_id = #{supplierId}")
    Integer sumTotalQuantityBySupplier(@Param("supplierId") Integer supplierId);

    /**
     * 统计某商品的入库总数量（关联明细表）
     */
    @Select("SELECT COALESCE(SUM(d.quantity), 0) " +
            "FROM stock_in_detail d " +
            "JOIN stock_in si ON d.stock_in_id = si.id " +
            "WHERE d.product_id = #{productId}")
    Integer sumQuantityByProduct(@Param("productId") Integer productId);
}