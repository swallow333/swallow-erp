package com.swalllow_erp.dto.request;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:58
 */
@Data
public class StockInQueryRequest {
    /**
     * 入库单号（模糊搜索）
     */
    private String inNo;
    /**
     * 供应商ID（精确搜索）
     */
    private Integer supplierId;
    /**
     * 采购订单ID（精确搜索）
     */
    private Integer purchaseOrderId;
    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 当前页码（默认1）
     */
    private Integer pageNum = 1;
    /**
     * 每页大小（默认10）
     */
    private Integer pageSize = 10;
}