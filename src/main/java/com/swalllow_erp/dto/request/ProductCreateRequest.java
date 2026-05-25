package com.swalllow_erp.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 21:24
 */
@Data
public class ProductCreateRequest {
    private String asin;
    private String sku;
    private String title;
    private String brand;
    private String category;
    private String imageUrl;
    private BigDecimal price;
    private BigDecimal costPrice;
    private List<String> bulletPoints;    // 五点描述列表
    private String description;            // 详细描述
    private String searchTerms;            // 搜索关键词
}