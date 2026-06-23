package com.swalllow_erp.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.SaleOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:31
 */

@Mapper
public interface SaleOrderMapper extends BaseMapper<SaleOrder> {

    /**
     * 根据订单编号查询
     */
    @Select("SELECT * FROM sale_order WHERE order_no = #{orderNo}")
    SaleOrder findByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 统计今日订单数量（用于生成单号）
     */
    @Select("SELECT COUNT(*) FROM sale_order WHERE order_no LIKE CONCAT(#{date}, '%')")
    int countTodayOrders(@Param("date") String date);
}