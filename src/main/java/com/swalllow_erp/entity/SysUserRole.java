package com.swalllow_erp.entity;

import lombok.Data;

import java.time.LocalDateTime;
/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:56
 */

/**
 * 用户-角色关联实体类
 *
 * @author Swallow333
 */
@Data
public class SysUserRole {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}