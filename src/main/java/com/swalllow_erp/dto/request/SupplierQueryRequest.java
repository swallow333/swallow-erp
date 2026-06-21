package com.swalllow_erp.dto.request;


import lombok.Data;
/**
 * @Author: Swallow333
 * @Date: 2026/06/22 1:39
 */

@Data
public class SupplierQueryRequest {
    private String keyword;      // 名称或编码模糊搜索
    private Integer status;      // 状态筛选
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}