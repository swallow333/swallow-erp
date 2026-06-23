package com.swalllow_erp.dto.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 2:31
 */
@Data
public class InventoryExportDTO {
    @ExcelProperty(value = "商品SKU", index = 0)
    private String productSku;
    @ExcelProperty(value = "商品名称", index = 1)
    private String productName;
    @ExcelProperty(value = "库存数量", index = 2)
    private Integer quantity;
    @ExcelProperty(value = "锁定数量", index = 3)
    private Integer lockedQuantity;
    @ExcelProperty(value = "可用数量", index = 4)
    private Integer availableQuantity;
}