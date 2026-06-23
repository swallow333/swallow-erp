package com.swalllow_erp.entity;


import lombok.Data;
import java.time.LocalDateTime;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:13
 */
@Data
public class SysOperationLog {
    private Long id;
    private Integer userId;
    private String username;
    private String module;
    private String operationType;
    private String description;
    private String url;
    private String method;
    private String params;
    private String ip;
    private Integer duration;
    private Integer status;
    private String errorMsg;
    private LocalDateTime createTime;
}