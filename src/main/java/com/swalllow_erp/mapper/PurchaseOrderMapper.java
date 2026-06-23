package com.swalllow_erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:17
 */
@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
    @Select("SELECT * FROM purchase_order WHERE order_no = #{orderNo}")
    PurchaseOrder findByOrderNo(@Param("orderNo") String orderNo);

    // 新增以下方法
    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM purchase_order WHERE status IN (${statuses})")
    BigDecimal sumTotalAmountByStatuses(@Param("statuses") List<Integer> statuses);

    @Select("SELECT COUNT(*) FROM purchase_order WHERE status = #{status}")
    Integer countByStatus(@Param("status") Integer status);
}