package com.swalllow_erp.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/01 5:10
 */

@Data
public class ProductCategory {
    private Integer id;
    private String code;
    private String name;
    private Integer parentId;
    private Integer level;
    private Integer sortOrder;
    private String icon;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // 非数据库字段：用于树形结构
    private List<ProductCategory> children;
}