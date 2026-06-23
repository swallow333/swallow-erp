package com.swalllow_erp.dto.request;

import lombok.Data;



/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:13
 */
@Data
public class OperationLogQueryRequest {
    private String username;
    private String module;
    private String operationType;
    private String startDate;
    private String endDate;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}