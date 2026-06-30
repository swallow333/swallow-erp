package com.swalllow_erp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 21:56
 */

@Data
public class StockIn {
    private Integer id;
    private String inNo;
    private Integer purchaseOrderId;
    private Integer supplierId;
    private LocalDateTime inDate;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    private Integer createBy;
    private LocalDateTime createTime;
    private Integer updateBy;
    private LocalDateTime updateTime;

    // 非数据库字段
    @TableField(exist = false)
    private String supplierName;
    @TableField(exist = false)
    private String purchaseOrderNo;
    @TableField(exist = false)
    private List<StockInDetail> details;
}