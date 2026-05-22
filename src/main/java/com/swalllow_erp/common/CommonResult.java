package com.swalllow_erp.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 统一响应结果类
 * @Author: Swallow333
 * @Date: 2026/05/22 19:05
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    private int code;   // 状态码
    private String message; // 提示消息
    private T data; //数据对象
    // ========== 成功方法 ==========
    public static <T> CommonResult<T> success() {
        return new CommonResult<>(0, "操作成功", null);
    }
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(0, "操作成功", data);
    }
    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(0, message, data);
    }
    // ========== 失败方法 ==========
    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(500, message, null);
    }
    public static <T> CommonResult<T> error(String message, T data) {
        return new CommonResult<>(500, message, data);
    }
}