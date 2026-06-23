package com.swalllow_erp.controller;

import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.entity.Inventory;
import com.swalllow_erp.entity.StockFlow;
import com.swalllow_erp.service.InventoryService;
import com.swalllow_erp.dto.request.InventoryQueryRequest;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:13
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    /**
     * 实时库存查询（分页 + 筛选）
     */
    @PostMapping("/page")
    public CommonResult<PageInfo<Inventory>> queryPage(@RequestBody InventoryQueryRequest request) {
        PageInfo<Inventory> page = inventoryService.queryPage(request);
        return CommonResult.success(page);
    }

    /**
     * 查询单个商品的库存
     */
    @GetMapping("/product/{productId}")
    public CommonResult<Inventory> getByProductId(@PathVariable Integer productId) {
        Inventory inventory = inventoryService.getByProductId(productId);
        if (inventory == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        return CommonResult.success(inventory);
    }

    /**
     * 查询库存流水
     */
    @GetMapping("/flows")
    public CommonResult<PageInfo<StockFlow>> queryFlows(
            @RequestParam(required = false) Integer productId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<StockFlow> page = inventoryService.queryFlows(productId, pageNum, pageSize);
        return CommonResult.success(page);
    }
}