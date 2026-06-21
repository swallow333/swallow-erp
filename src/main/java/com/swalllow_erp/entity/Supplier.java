package com.swalllow_erp.entity;


import lombok.Data;
import java.time.LocalDateTime;
/**
 * @Author: Swallow333
 * @Date: 2026/06/22 1:36
 */

@Data
public class Supplier {
    private Integer id;
    private String code;
    private String name;
    private String contact;
    private String phone;
    private String email;
    private String address;
    private String taxNo;
    private String bankName;
    private String bankAccount;
    private String paymentTerms;
    private Integer leadDays;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}