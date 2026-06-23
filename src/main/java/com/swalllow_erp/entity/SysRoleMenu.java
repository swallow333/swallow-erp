package com.swalllow_erp.entity;


import lombok.Data;
import java.time.LocalDateTime;


/**
 * @Author: Swallow333
 * @Date: 2026/06/24 2:21
 */

/**
 * 角色-菜单关联实体类
 *
 * @author Swallow333
 */
@Data
public class SysRoleMenu {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 菜单ID
     */
    private Integer menuId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}