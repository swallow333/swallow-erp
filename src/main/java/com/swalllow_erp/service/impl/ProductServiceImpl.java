package com.swalllow_erp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swalllow_erp.entity.Product;
import com.swalllow_erp.mapper.ProductMapper;
import com.swalllow_erp.service.ProductService;
import org.springframework.stereotype.Service;

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
}