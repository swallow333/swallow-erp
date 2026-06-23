package com.swalllow_erp.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 21:57
 */

@Data
public class Inventory {
    private Integer id;
    private Integer productId;
    private Integer quantity;
    private Integer lockedQuantity;
    private Integer availableQuantity;
    private LocalDateTime updateTime;

    // 非数据库字段
    private String productName;
    private String productSku;
}