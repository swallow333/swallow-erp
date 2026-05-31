package com.swalllow_erp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.ProductCategory;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/01 5:14
 */

public interface ProductCategoryService extends IService<ProductCategory> {

    /**
     * 获取树形分类列表
     */
    List<ProductCategory> getTreeList();

    /**
     * 获取某分类的子分类
     */
    List<ProductCategory> getChildren(Integer parentId);

    /**
     * 检查编码是否重复
     */
    boolean isCodeDuplicate(String code, Integer excludeId);
}