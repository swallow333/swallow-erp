package com.swalllow_erp.common;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局响应统一包装
 * @Author: Swallow333
 * @Date: 2026/05/22 18:58
 * @description: 自动将 Controller 返回的数据包装成 CommonResult 格式
 */

@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要包装
     *
     * @param returnType 方法返回类型
     * @param converterType 转换器类型
     * @return true=需要包装，false=不需要包装
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 1. 如果已经是 CommonResult 类型，不再包装（避免重复包装）
        if (returnType.getParameterType().isAssignableFrom(CommonResult.class)) {
            return false;
        }

        // 2. 如果是 Swagger/Knife4j 相关接口，不包装
        String controllerName = returnType.getDeclaringClass().getSimpleName();
        if (controllerName.startsWith("Swagger") || controllerName.startsWith("Api") ||
                controllerName.startsWith("Springfox") || controllerName.startsWith("Knife4j")) {
            return false;
        }

        // 3. 如果是 String 类型，需要特殊处理（下面会单独处理）
        if (returnType.getParameterType().isAssignableFrom(String.class)) {
            return true;
        }

        // 4. 其他情况都包装
        return true;
    }

    /**
     * 包装响应体
     *
     * @param body 原始响应体（Controller 返回的对象）
     * @param returnType 方法返回类型
     * @param selectedContentType 内容类型
     * @param selectedConverterType 转换器类型
     * @param request HTTP 请求
     * @param response HTTP 响应
     * @return 包装后的 CommonResult 对象
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 1. 如果响应体为 null，返回空成功
        if (body == null) {
            return CommonResult.success();
        }

        // 2. 如果已经是 CommonResult 类型，直接返回
        if (body instanceof CommonResult) {
            return body;
        }

        // 3. 如果是 String 类型，需要特殊处理
        //    Spring 的 StringHttpMessageConverter 不能直接处理 CommonResult
        if (body instanceof String) {
            // 把 CommonResult 转成 JSON 字符串返回
            return com.alibaba.fastjson.JSON.toJSONString(CommonResult.success(body));
        }

        // 4. 其他情况：包装成成功的 CommonResult
        return CommonResult.success(body);
    }
}