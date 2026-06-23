package com.swalllow_erp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.StockInDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:01
 */

@Mapper
public interface StockInDetailMapper extends BaseMapper<StockInDetail> {

    @Select("SELECT * FROM stock_in_detail WHERE stock_in_id = #{stockInId}")
    List<StockInDetail> selectByStockInId(@Param("stockInId") Integer stockInId);
}