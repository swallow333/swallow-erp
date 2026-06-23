package com.swalllow_erp.dto.export;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:30
 */

/**
 * 导出采购订单
 */
@Data
public class PurchaseOrderExportDTO {

    @ExcelProperty(value = "订单编号", index = 0)
    private String orderNo;

    @ExcelProperty(value = "供应商", index = 1)
    private String supplierName;

    @ExcelProperty(value = "总金额", index = 2)
    private BigDecimal totalAmount;

    @ExcelProperty(value = "状态", index = 3)
    private String statusName;

    @ExcelProperty(value = "创建时间", index = 4)
    private String createTime;

    @ExcelProperty(value = "备注", index = 5)
    private String remark;
}