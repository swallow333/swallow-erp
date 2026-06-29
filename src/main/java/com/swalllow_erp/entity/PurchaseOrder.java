package com.swalllow_erp.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 21:31
 */
@Data
public class PurchaseOrder {
    private Integer id;
    private String orderNo;
    private Integer supplierId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    private Integer createBy;
    private LocalDateTime createTime;
    private Integer updateBy;
    private LocalDateTime updateTime;

    // 非数据库字段（关联查询用）
    @TableField(exist = false)  // 表明该字段在数据库中不存在
    private String supplierName;
    @TableField(exist = false)  // TODO：数据库设计不合理，修改数据库
    private List<PurchaseOrderDetail> details;
}