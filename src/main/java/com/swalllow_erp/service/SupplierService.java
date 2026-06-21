package com.swalllow_erp.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.Supplier;
import com.swalllow_erp.dto.request.SupplierQueryRequest;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 1:41
 */

public interface SupplierService extends IService<Supplier> {

    /**
     * 根据编码查询供应商
     */
    Supplier findByCode(String code);

    /**
     * 分页查询供应商
     */
    PageInfo<Supplier> queryPage(SupplierQueryRequest request);

    /**
     * 查询启用的供应商（用于下拉框）
     */
    List<Supplier> getEnabledList();

    /**
     * 检查编码是否重复
     */
    boolean isCodeDuplicate(String code, Integer excludeId);
}