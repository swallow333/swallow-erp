package com.swalllow_erp.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.PurchaseOrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:17
 */

@Mapper
public interface PurchaseOrderDetailMapper extends BaseMapper<PurchaseOrderDetail> {

    @Select("SELECT * FROM purchase_order_detail WHERE order_id = #{orderId}")
    List<PurchaseOrderDetail> selectByOrderId(@Param("orderId") Integer orderId);
}