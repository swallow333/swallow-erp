package com.swalllow_erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 21:25
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    @Select("SELECT * FROM product WHERE asin = #{asin}")
    List<Product> findByAsin(@Param("asin") String asin);

    @Select("SELECT * FROM product WHERE sku = #{sku}")
    Product findBySku(@Param("sku") String sku);
}