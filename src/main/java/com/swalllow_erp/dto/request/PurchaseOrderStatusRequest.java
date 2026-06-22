package com.swalllow_erp.dto.request;



import lombok.Data;



/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:16
 */
@Data
public class PurchaseOrderStatusRequest {
    private Integer status;
    private String remark;
}