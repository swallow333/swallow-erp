package com.swalllow_erp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.Product;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 21:31
 */
public interface ProductService extends IService<Product> {
    Product findBySku(String sku);
    List<Product> findByAsin(String asin);
}