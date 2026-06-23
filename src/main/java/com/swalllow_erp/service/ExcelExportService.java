package com.swalllow_erp.service;

import com.swalllow_erp.dto.request.InventoryQueryRequest;
import com.swalllow_erp.dto.request.PurchaseOrderQueryRequest;
import com.swalllow_erp.dto.request.SaleOrderQueryRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:30
 */

public interface ExcelExportService {
    /**
     * 导出采购订单
     */
    void exportPurchaseOrders(PurchaseOrderQueryRequest request, HttpServletResponse response);

    /**
     * 导出销售订单
     */
    void exportSaleOrders(SaleOrderQueryRequest request, HttpServletResponse response);

    /**
     * 导出库存报表
     */
    void exportInventory(InventoryQueryRequest request, HttpServletResponse response);
}