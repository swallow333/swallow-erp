package com.swalllow_erp.dto.request;

/**
 * @Author: Swallow333
 * @Date: 2026/05/23 20:15
 */

import lombok.Data;

/**
 * 登录请求参数
 *
 * @author Swallow333
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}