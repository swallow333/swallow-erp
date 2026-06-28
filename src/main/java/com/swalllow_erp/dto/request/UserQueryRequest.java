package com.swalllow_erp.dto.request;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/28 23:14
 */

@Data
public class UserQueryRequest {

    /**
     * 关键词（用户名或昵称模糊搜索）
     */
    private String keyword;

    /**
     * 状态：1正常 0禁用
     */
    private Integer status;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}