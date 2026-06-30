package com.swalllow_erp.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.PurchaseOrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:17
 */

@Mapper
public interface PurchaseOrderDetailMapper extends BaseMapper<PurchaseOrderDetail> {

    @Select("SELECT * FROM purchase_order_detail WHERE order_id = #{orderId}")
    List<PurchaseOrderDetail> selectByOrderId(@Param("orderId") Integer orderId);

    // 增加已入库数量
    @Update("UPDATE purchase_order_detail SET received_quantity = received_quantity + #{quantity} " +
            "WHERE id = #{detailId}")
    int increaseReceivedQuantity(@Param("detailId") Integer detailId, @Param("quantity") Integer quantity);

    // 查询订单详情（可选）
    @Select("SELECT * FROM purchase_order_detail WHERE order_id = #{orderId} AND product_id = #{productId}")
    List<PurchaseOrderDetail> selectByOrderAndProduct(@Param("orderId") Integer orderId,
                                                      @Param("productId") Integer productId);
}