package com.swalllow_erp.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;



/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:17
 */
@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {

    @Select("SELECT * FROM purchase_order WHERE order_no = #{orderNo}")
    PurchaseOrder findByOrderNo(@Param("orderNo") String orderNo);
}