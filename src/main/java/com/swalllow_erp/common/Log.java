package com.swalllow_erp.common;

import java.lang.annotation.*;



/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 操作描述
     */
    String description() default "";

    /**
     * 是否保存请求参数
     */
    boolean saveParams() default true;
}