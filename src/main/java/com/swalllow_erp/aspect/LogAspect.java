package com.swalllow_erp.aspect;

import com.alibaba.fastjson2.JSON;
import com.swalllow_erp.common.Log;
import com.swalllow_erp.entity.SysOperationLog;
import com.swalllow_erp.service.SysOperationLogService;
import com.swalllow_erp.utils.ServletUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:17
 */

@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysOperationLogService logService;

    @Around("@annotation(com.swalllow_erp.common.Log)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        HttpServletRequest request = ServletUtils.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);

        // 获取操作用户（从请求中获取，需要实现）
        Integer userId = (Integer) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");

        // 构建日志对象
        SysOperationLog log = new SysOperationLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setModule(logAnnotation.module());
        log.setDescription(logAnnotation.description());
        log.setUrl(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setIp(ServletUtils.getClientIP(request));

        // 是否保存请求参数
        if (logAnnotation.saveParams()) {
            Object[] args = joinPoint.getArgs();
            // 过滤掉 HttpServletRequest/Response 等大对象
            String params = JSON.toJSONString(args);
            log.setParams(params);
        }

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            log.setDuration((int) (endTime - startTime));
            log.setStatus(1);
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.setDuration((int) (endTime - startTime));
            log.setStatus(0);
            log.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            // 异步保存日志（不阻塞主业务）
            logService.log(log);
        }
    }
}