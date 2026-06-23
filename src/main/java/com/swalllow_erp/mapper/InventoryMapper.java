package com.swalllow_erp.mapper;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:00
 */
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

    @Select("SELECT * FROM inventory WHERE product_id = #{productId}")
    Inventory selectByProductId(@Param("productId") Integer productId);
}