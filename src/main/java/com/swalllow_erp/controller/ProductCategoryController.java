package com.swalllow_erp.controller;

import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.request.CategoryCreateRequest;
import com.swalllow_erp.dto.request.CategoryUpdateRequest;
import com.swalllow_erp.entity.ProductCategory;
import com.swalllow_erp.service.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/01 5:16
 */
@RestController
@RequestMapping("/categories")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService categoryService;

    /**
     * 获取分类树（树形结构）
     */
    @GetMapping("/tree")
    public CommonResult<List<ProductCategory>> getTree() {
        List<ProductCategory> tree = categoryService.getTreeList();
        return CommonResult.success(tree);
    }

    /**
     * 获取子分类列表（平铺，不递归）
     */
    @GetMapping("/children/{parentId}")
    public CommonResult<List<ProductCategory>> getChildren(@PathVariable Integer parentId) {
        List<ProductCategory> children = categoryService.getChildren(parentId);
        return CommonResult.success(children);
    }

    /**
     * 获取所有分类（平铺）
     */
    @GetMapping
    public CommonResult<List<ProductCategory>> list() {
        List<ProductCategory> list = categoryService.list();
        return CommonResult.success(list);
    }

    /**
     * 获取单个分类
     */
    @GetMapping("/{id}")
    public CommonResult<ProductCategory> getById(@PathVariable Integer id) {
        ProductCategory category = categoryService.getById(id);
        if (category == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        return CommonResult.success(category);
    }

    /**
     * 新增分类
     */
    @PostMapping
    public CommonResult<Void> create(@RequestBody @Valid CategoryCreateRequest request) {
        // 检查编码是否重复
        if (categoryService.isCodeDuplicate(request.getCode(), null)) {
            return CommonResult.error(CommonCodeEnum.DATA_DUPLICATE);
        }

        ProductCategory category = new ProductCategory();
        category.setCode(request.getCode());
        category.setName(request.getName());
        category.setParentId(request.getParentId() != null ? request.getParentId() : 0);
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        category.setIcon(request.getIcon());
        category.setStatus(1);

        categoryService.save(category);
        return CommonResult.success("创建成功", null);
    }

    /**
     * 修改分类
     */
    @PutMapping("/{id}")
    public CommonResult<Void> update(@PathVariable Integer id,
                                     @RequestBody CategoryUpdateRequest request) {
        ProductCategory category = categoryService.getById(id);
        if (category == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }

        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getParentId() != null) {
            category.setParentId(request.getParentId());
        }
        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }
        if (request.getStatus() != null) {
            category.setStatus(request.getStatus());
        }

        categoryService.updateById(category);
        return CommonResult.success("修改成功", null);
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> delete(@PathVariable Integer id) {
        ProductCategory category = categoryService.getById(id);
        if (category == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }

        // 检查是否有子分类
        List<ProductCategory> children = categoryService.getChildren(id);
        if (!children.isEmpty()) {
            return CommonResult.error("请先删除子分类");
        }

        // 检查是否有商品使用此分类（需要 Product 表有关联）
        // TODO: 检查 product 表中 category 字段是否有引用

        categoryService.removeById(id);
        return CommonResult.success("删除成功", null);
    }

    /**
     * 启用/禁用分类
     */
    @PutMapping("/{id}/status")
    public CommonResult<Void> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        ProductCategory category = categoryService.getById(id);
        if (category == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        category.setStatus(status);
        categoryService.updateById(category);
        return CommonResult.success(status == 1 ? "启用成功" : "禁用成功", null);
    }
}