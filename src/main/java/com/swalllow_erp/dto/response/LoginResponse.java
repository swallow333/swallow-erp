package com.swalllow_erp.dto.response;

/**
 * @Author: Swallow333
 * @Date: 2026/05/23 20:15
 */

import com.swalllow_erp.entity.SysUser;
import lombok.Data;

/**
 * 登录响应参数
 *
 * @author Swallow333
 */
@Data
public class LoginResponse {

    /**
     * 认证 Token
     */
    private String token;

    /**
     * 用户信息
     */
    private SysUser user;

    public LoginResponse(String token, SysUser user) {
        this.token = token;
        this.user = user;
    }
}