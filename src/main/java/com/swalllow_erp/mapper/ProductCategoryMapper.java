package com.swalllow_erp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swalllow_erp.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/01 5:13
 */

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

    @Select("SELECT * FROM product_category WHERE parent_id = #{parentId} ORDER BY sort_order ASC")
    List<ProductCategory> selectByParentId(Integer parentId);

    @Select("SELECT * FROM product_category WHERE status = 1 ORDER BY sort_order ASC")
    List<ProductCategory> selectEnabledCategories();
}