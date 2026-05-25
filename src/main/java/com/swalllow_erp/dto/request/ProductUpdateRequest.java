package com.swalllow_erp.dto.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 21:24
 */
@Data
public class ProductUpdateRequest {
    private String title;
    private String brand;
    private String imageUrl;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer status;
    private List<String> bulletPoints;
    private String description;
    private String searchTerms;
}