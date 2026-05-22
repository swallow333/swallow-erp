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
// 泛型类
public class CommonResult<T> {
    private int code;
    private String message;
    private T data;
    // ========== 成功方法 ==========
    // 第一个T声明这个方法是泛型方法，第二个T表示返回值是CommonResult<T>类型
    public static <T> CommonResult<T> success() {
        return new CommonResult<>(200, "操作成功", null);
    }
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(200, "操作成功", data);
    }
    public static <T> CommonResult<T> success(String message, T data) {
        return new CommonResult<>(200, message, data);
    }
    // ========== 失败方法（使用枚举）==========
    public static <T> CommonResult<T> error(CommonCodeEnum codeEnum) {
        return new CommonResult<>(codeEnum.getCode(), codeEnum.getMessage(), null);
    }
    public static <T> CommonResult<T> error(CommonCodeEnum codeEnum, String message) {
        return new CommonResult<>(codeEnum.getCode(), message, null);
    }
    // ========== 失败方法（使用 String）==========
    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(500, message, null);
    }
    public static <T> CommonResult<T> error(int code, String message) {
        return new CommonResult<>(code, message, null);
    }
}