package com.swalllow_erp.dto.request;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:14
 */
@Data
public class PurchaseOrderCreateRequest {
    @NotNull(message = "供应商不能为空")
    private Integer supplierId;
    private String remark;
    @NotEmpty(message = "采购明细不能为空")
    @Valid
    private List<OrderDetailRequest> details;

    @Data
    public static class OrderDetailRequest {
        @NotNull(message = "商品不能为空")
        private Integer productId;
        @NotNull(message = "数量不能为空")
        private Integer quantity;
        @NotNull(message = "单价不能为空")
        private BigDecimal price;
    }
}