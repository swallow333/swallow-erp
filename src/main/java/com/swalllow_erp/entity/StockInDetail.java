package com.swalllow_erp.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 21:57
 */
@Data
public class StockInDetail {
    private Integer id;
    private Integer stockInId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 非数据库字段
    @TableField(exist = false)
    private String productName;
    @TableField(exist = false)
    private String productSku;
}