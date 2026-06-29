package com.swalllow_erp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swalllow_erp.dto.request.ProductQueryRequest;
import com.swalllow_erp.entity.Product;
import com.swalllow_erp.mapper.ProductMapper;
import com.swalllow_erp.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 21:32
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements ProductService {

    @Override
    public Product findBySku(String sku) {
        return baseMapper.findBySku(sku);
    }

    @Override
    public List<Product> findByAsin(String asin) {
        return baseMapper.findByAsin(asin);
    }

    @Override
    public PageInfo<Product> queryPage(ProductQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.like(Product::getTitle, request.getKeyword())
                    .or()
                    .like(Product::getSku, request.getKeyword());
        }
        if (request.getStatus() != null) {
            wrapper.eq(Product::getStatus, request.getStatus());
        }
        wrapper.orderByDesc(Product::getCreateTime);
        List<Product> list = list(wrapper);
        return new PageInfo<>(list);
    }
}