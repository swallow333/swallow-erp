package com.swalllow_erp.entity;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 19:27
 */

@Data
public class Product {
    private Integer id;
    private String asin;
    private String sku;
    private String title;
    private String brand;
    private String category;
    private String imageUrl;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer status;
    private LocalDateTime syncTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}