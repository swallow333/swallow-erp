package com.swalllow_erp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.ProductListing;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 21:33
 */
public interface ProductListingService extends IService<ProductListing> {
    ProductListing findByProductId(Integer productId);
}