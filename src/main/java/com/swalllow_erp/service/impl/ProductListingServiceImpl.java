package com.swalllow_erp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swalllow_erp.entity.ProductListing;
import com.swalllow_erp.mapper.ProductListingMapper;
import com.swalllow_erp.service.ProductListingService;
import org.springframework.stereotype.Service;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 21:34
 */

@Service
public class ProductListingServiceImpl extends ServiceImpl<ProductListingMapper, ProductListing>
        implements ProductListingService {

    @Override
    public ProductListing findByProductId(Integer productId) {
        LambdaQueryWrapper<ProductListing> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProductListing::getProductId, productId);
        return this.getOne(wrapper);
    }
}