package com.swalllow_erp.dto.request;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/01 5:11
 */

@Data
public class CategoryCreateRequest {
    private String code;
    private String name;
    private Integer parentId;  // 0 表示顶级分类
    private Integer sortOrder;
    private String icon;
}