package com.swalllow_erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:00
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
    @Select("SELECT * FROM inventory WHERE product_id = #{productId}")
    Inventory selectByProductId(@Param("productId") Integer productId);

    // 新增方法
    @Select("SELECT COUNT(*) FROM inventory WHERE quantity < #{threshold}")
    Integer countLowStock(@Param("threshold") Integer threshold);

    // InventoryMapper.java
    @Select("SELECT i.*, p.sku AS productSku, p.title AS productName " +
            "FROM inventory i " +
            "LEFT JOIN product p ON i.product_id = p.id " +
            "ORDER BY i.update_time DESC")
    List<Inventory> selectListWithProduct();
}