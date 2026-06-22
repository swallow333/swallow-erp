package com.swalllow_erp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.PurchaseOrder;
import com.swalllow_erp.entity.PurchaseOrderDetail;
import com.swalllow_erp.dto.request.PurchaseOrderCreateRequest;
import com.swalllow_erp.dto.request.PurchaseOrderQueryRequest;
import com.swalllow_erp.dto.request.PurchaseOrderStatusRequest;
import com.github.pagehelper.PageInfo;
import java.util.List;



/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:18
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {

    /**
     * 创建采购订单
     */
    PurchaseOrder createOrder(PurchaseOrderCreateRequest request, Integer userId);

    /**
     * 分页查询采购订单
     */
    PageInfo<PurchaseOrder> queryPage(PurchaseOrderQueryRequest request);

    /**
     * 根据ID查询采购订单（含明细）
     */
    PurchaseOrder getOrderWithDetails(Integer orderId);

    /**
     * 更新采购订单状态
     */
    void updateStatus(Integer orderId, PurchaseOrderStatusRequest request, Integer userId);

    /**
     * 取消采购订单
     */
    void cancelOrder(Integer orderId, Integer userId);

    /**
     * 生成订单编号
     */
    String generateOrderNo();
}