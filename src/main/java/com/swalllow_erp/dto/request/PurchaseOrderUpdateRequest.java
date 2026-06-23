package com.swalllow_erp.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:15
 */
@Data
public class PurchaseOrderUpdateRequest {
    private Integer supplierId;
    private String remark;
    private List<OrderDetailRequest> details;

    @Data
    public static class OrderDetailRequest {
        private Integer id;       // 存在则更新，不存在则新增
        private Integer productId;
        private Integer quantity;
        private BigDecimal price;
        private Boolean deleted;  // 是否删除
    }
}