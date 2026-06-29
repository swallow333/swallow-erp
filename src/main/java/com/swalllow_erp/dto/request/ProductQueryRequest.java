package com.swalllow_erp.dto.request;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/30 3:36
 */


@Data
public class ProductQueryRequest {
    private String keyword;
    private Integer categoryId;
    private Integer status;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}