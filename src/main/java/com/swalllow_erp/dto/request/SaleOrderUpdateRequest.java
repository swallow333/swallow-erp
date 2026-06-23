package com.swalllow_erp.dto.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:27
 */
@Data
public class SaleOrderUpdateRequest {
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String remark;
    private List<OrderDetail> details;

    @Data
    public static class OrderDetail {
        private Integer id;
        private Integer productId;
        private Integer quantity;
        private BigDecimal price;
        private Boolean deleted;
    }
}