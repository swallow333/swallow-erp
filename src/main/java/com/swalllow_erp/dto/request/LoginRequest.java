package com.swalllow_erp.dto.request;

/**
 * 登录请求参数
 * @Author: Swallow333
 * @Date: 2026/05/23 20:15
 * @description: DTO隔离数据库实体与前端交互,防止数据冗余，安全漏洞等问题。
 */

import lombok.Data;

@Data
public class LoginRequest {

    // 用户名
    private String username;

    // 密码
    private String password;
}