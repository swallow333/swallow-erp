package com.swalllow_erp.entity;

import lombok.Data;
import java.time.LocalDateTime;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 21:58
 */
@Data
public class StockFlow {
    private Integer id;
    private Integer productId;
    private Integer flowType;
    private Integer changeQuantity;
    private Integer beforeQuantity;
    private Integer afterQuantity;
    private String sourceType;
    private Integer sourceId;
    private String remark;
    private LocalDateTime createTime;
}