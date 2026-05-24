package com.swalllow_erp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 5:55
 */
@Data
public class UserCreateRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String nickname;
    private String phone;
}