package com.swalllow_erp.dto.request;


package com.swalllow_erp.dto.request;

import lombok.Data;


/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:16
 */
@Data
public class PurchaseOrderQueryRequest {
    private String orderNo;
    private Integer supplierId;
    private Integer status;
    private String startDate;
    private String endDate;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}