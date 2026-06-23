package com.swalllow_erp.controller;


import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.request.StockInCreateRequest;
import com.swalllow_erp.dto.request.StockInQueryRequest;
import com.swalllow_erp.entity.StockIn;
import com.swalllow_erp.service.StockInService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:02
 */

@RestController
@RequestMapping("/stock-in")
public class StockInController {

    @Autowired
    private StockInService stockInService;

    /**
     * 创建入库单（采购入库）
     */
    @PostMapping
    public CommonResult<StockIn> create(@RequestBody @Valid StockInCreateRequest request) {
        Integer userId = 1;  // TODO: 从上下文获取
        StockIn stockIn = stockInService.createStockIn(request, userId);
        return CommonResult.success(stockIn);
    }

    /**
     * 分页查询入库单
     */
    @PostMapping("/page")
    public CommonResult<PageInfo<StockIn>> queryPage(@RequestBody StockInQueryRequest request) {
        PageInfo<StockIn> page = stockInService.queryPage(request);
        return CommonResult.success(page);
    }

    /**
     * 查询入库单详情（含明细）
     */
    @GetMapping("/{id}")
    public CommonResult<StockIn> getDetail(@PathVariable Integer id) {
        StockIn stockIn = stockInService.getStockInWithDetails(id);
        if (stockIn == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        return CommonResult.success(stockIn);
    }
}