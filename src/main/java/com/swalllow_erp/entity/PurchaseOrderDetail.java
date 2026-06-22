package com.swalllow_erp.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;



/**
 * @Author: Swallow333
 * @Date: 2026/06/22 21:32
 */
@Data
public class PurchaseOrderDetail {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private Integer receivedQuantity;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 非数据库字段（关联查询用）
    private String productName;
    private String productSku;
}