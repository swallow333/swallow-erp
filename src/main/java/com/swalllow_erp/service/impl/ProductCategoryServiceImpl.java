package com.swalllow_erp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swalllow_erp.entity.ProductCategory;
import com.swalllow_erp.mapper.ProductCategoryMapper;
import com.swalllow_erp.service.ProductCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Swallow333
 * @Date: 2026/06/01 5:15
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory>
        implements ProductCategoryService {

    @Override
    public List<ProductCategory> getTreeList() {
        // 查询所有启用的分类
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductCategory::getStatus, 1)
                .orderByAsc(ProductCategory::getSortOrder);
        List<ProductCategory> all = list(wrapper);

        // 构建树形结构
        return buildTree(all, 0);
    }

    @Override
    public List<ProductCategory> getChildren(Integer parentId) {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductCategory::getParentId, parentId)
                .orderByAsc(ProductCategory::getSortOrder);
        return list(wrapper);
    }

    @Override
    public boolean isCodeDuplicate(String code, Integer excludeId) {
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductCategory::getCode, code);
        if (excludeId != null) {
            wrapper.ne(ProductCategory::getId, excludeId);
        }
        return count(wrapper) > 0;
    }

    /**
     * 递归构建树形结构
     */
    private List<ProductCategory> buildTree(List<ProductCategory> all, Integer parentId) {
        return all.stream()
                .filter(category -> category.getParentId().equals(parentId))
                .peek(category -> {
                    List<ProductCategory> children = buildTree(all, category.getId());
                    if (!children.isEmpty()) {
                        category.setChildren(children);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean save(ProductCategory entity) {
        // 设置层级
        if (entity.getParentId() == null || entity.getParentId() == 0) {
            entity.setLevel(1);
        } else {
            ProductCategory parent = getById(entity.getParentId());
            if (parent != null) {
                entity.setLevel(parent.getLevel() + 1);
            } else {
                entity.setLevel(1);
            }
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(ProductCategory entity) {
        // 如果修改了父分类，需要重新计算层级
        ProductCategory old = getById(entity.getId());
        if (old != null && !old.getParentId().equals(entity.getParentId())) {
            if (entity.getParentId() == null || entity.getParentId() == 0) {
                entity.setLevel(1);
            } else {
                ProductCategory parent = getById(entity.getParentId());
                entity.setLevel(parent != null ? parent.getLevel() + 1 : 1);
            }
        }
        return super.updateById(entity);
    }
}