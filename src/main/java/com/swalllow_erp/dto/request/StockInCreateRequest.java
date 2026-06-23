package com.swalllow_erp.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 21:58
 */
@Data
public class StockInCreateRequest {
    @NotNull(message = "采购订单不能为空")
    private Integer purchaseOrderId;
    private String remark;
    @NotNull(message = "入库明细不能为空")
    private List<StockInDetailRequest> details;

    @Data
    public static class StockInDetailRequest {
        @NotNull(message = "商品不能为空")
        private Integer productId;
        @NotNull(message = "数量不能为空")
        private Integer quantity;
        @NotNull(message = "单价不能为空")
        private BigDecimal price;
    }
}