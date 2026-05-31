package com.swalllow_erp.dto.request;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/01 5:12
 */

@Data
public class CategoryUpdateRequest {
    private String name;
    private Integer parentId;
    private Integer sortOrder;
    private String icon;
    private Integer status;
}
