package com.swalllow_erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.SaleOrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:31
 */

@Mapper
public interface SaleOrderDetailMapper extends BaseMapper<SaleOrderDetail> {

    /**
     * 根据订单ID查询明细
     */
    @Select("SELECT * FROM sale_order_detail WHERE order_id = #{orderId}")
    List<SaleOrderDetail> selectByOrderId(@Param("orderId") Integer orderId);

    /**
     * 批量插入明细
     */
    @Insert({
            "<script>",
            "INSERT INTO sale_order_detail (order_id, product_id, quantity, price, amount) VALUES",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.orderId}, #{item.productId}, #{item.quantity}, #{item.price}, #{item.amount})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int batchInsert(@Param("list") List<SaleOrderDetail> details);
}