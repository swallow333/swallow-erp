package com.swalllow_erp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Swallow333
 * @Date: 2026/05/23 20:11
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")                           // 拦截所有
                .excludePathPatterns("/auth/login")               // 不拦截登录
                .excludePathPatterns("/auth/refresh")             // 不拦截刷新
                .excludePathPatterns("/swagger-ui/**")            // 不拦截文档
                .excludePathPatterns("/v2/api-docs")              // 不拦截文档
                .excludePathPatterns("/doc.html")                 // 不拦截文档
                .order(1);
    }
}