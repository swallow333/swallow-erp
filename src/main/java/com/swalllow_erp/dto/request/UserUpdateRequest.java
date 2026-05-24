package com.swalllow_erp.dto.request;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 6:05
 */
@Data
public class UserUpdateRequest {
    private String nickname;
    private String phone;
    private String password;
    private Integer status;
}