package com.swalllow_erp.dto.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 2:32
 */
@Data
public class SaleOrderExportDTO {
    @ExcelProperty(value = "订单编号", index = 0)
    private String orderNo;
    @ExcelProperty(value = "客户名称", index = 1)
    private String customerName;
    @ExcelProperty(value = "总金额", index = 2)
    private BigDecimal totalAmount;
    @ExcelProperty(value = "状态", index = 3)
    private String statusName;
    @ExcelProperty(value = "创建时间", index = 4)
    private String createTime;
}