package com.swalllow_erp.common;

import com.swalllow_erp.exception.BusinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: Swallow333
 * @Date: 2026/06/30 6:27
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonResult<Void> handleBusinessException(BusinessException e) {
        return new CommonResult<>(e.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public CommonResult<Void> handleException(Exception e) {
        e.printStackTrace();
        return new CommonResult<>(500, "系统繁忙，请稍后重试", null);
    }
}