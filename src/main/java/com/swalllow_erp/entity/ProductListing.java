package com.swalllow_erp.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 21:23
 */
@Data
public class ProductListing {
    private Integer id;
    private Integer productId;
    private String bulletPoints;    // JSON格式存储
    private String description;
    private String searchTerms;
    private String variationTheme;
    private String parentAsin;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}