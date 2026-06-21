package com.swalllow_erp.dto.request;

import lombok.Data;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 1:38
 */

@Data
public class SupplierUpdateRequest {
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
}