package com.swalllow_erp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.StockIn;
import com.swalllow_erp.entity.StockFlow;
import com.swalllow_erp.dto.request.StockInCreateRequest;
import com.swalllow_erp.dto.request.StockInQueryRequest;
import com.github.pagehelper.PageInfo;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 21:59
 */

public interface StockInService extends IService<StockIn> {

    /**
     * 创建入库单
     */
    StockIn createStockIn(StockInCreateRequest request, Integer userId);

    /**
     * 分页查询入库单
     */
    PageInfo<StockIn> queryPage(StockInQueryRequest request);

    /**
     * 查询入库单详情（含明细）
     */
    StockIn getStockInWithDetails(Integer stockInId);

    /**
     * 生成入库单号
     */
    String generateInNo();
}