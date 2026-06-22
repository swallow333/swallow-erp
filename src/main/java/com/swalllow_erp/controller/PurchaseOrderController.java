package com.swalllow_erp.controller;



import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.request.PurchaseOrderCreateRequest;
import com.swalllow_erp.dto.request.PurchaseOrderQueryRequest;
import com.swalllow_erp.dto.request.PurchaseOrderStatusRequest;
import com.swalllow_erp.entity.PurchaseOrder;
import com.swalllow_erp.service.PurchaseOrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:19
 */

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    /**
     * 创建采购订单
     */
    @PostMapping
    public CommonResult<PurchaseOrder> create(@RequestBody @Valid PurchaseOrderCreateRequest request) {
        // TODO: 从上下文获取当前用户ID
        Integer userId = 1;
        PurchaseOrder order = purchaseOrderService.createOrder(request, userId);
        return CommonResult.success(order);
    }

    /**
     * 分页查询采购订单
     */
    @PostMapping("/page")
    public CommonResult<PageInfo<PurchaseOrder>> queryPage(@RequestBody PurchaseOrderQueryRequest request) {
        PageInfo<PurchaseOrder> page = purchaseOrderService.queryPage(request);
        return CommonResult.success(page);
    }

    /**
     * 查询采购订单详情（含明细）
     */
    @GetMapping("/{id}")
    public CommonResult<PurchaseOrder> getDetail(@PathVariable Integer id) {
        PurchaseOrder order = purchaseOrderService.getOrderWithDetails(id);
        if (order == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        return CommonResult.success(order);
    }

    /**
     * 更新订单状态（审核、入库）
     */
    @PutMapping("/{id}/status")
    public CommonResult<Void> updateStatus(@PathVariable Integer id,
                                           @RequestBody PurchaseOrderStatusRequest request) {
        Integer userId = 1;  // TODO: 从上下文获取
        purchaseOrderService.updateStatus(id, request, userId);
        return CommonResult.success("操作成功", null);
    }

    /**
     * 取消采购订单
     */
    @PutMapping("/{id}/cancel")
    public CommonResult<Void> cancel(@PathVariable Integer id) {
        Integer userId = 1;  // TODO: 从上下文获取
        purchaseOrderService.cancelOrder(id, userId);
        return CommonResult.success("取消成功", null);
    }
}