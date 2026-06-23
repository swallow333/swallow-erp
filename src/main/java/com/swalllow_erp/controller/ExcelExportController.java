package com.swalllow_erp.controller;

import com.swalllow_erp.common.Log;
import com.swalllow_erp.dto.request.InventoryQueryRequest;
import com.swalllow_erp.dto.request.PurchaseOrderQueryRequest;
import com.swalllow_erp.dto.request.SaleOrderQueryRequest;
import com.swalllow_erp.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;


/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:32
 */
@RestController
@RequestMapping("/export")
public class ExcelExportController {

    @Autowired
    private ExcelExportService excelExportService;

    @Log(module = "采购报表", description = "导出采购订单")
    @PostMapping("/purchase-orders")
    public void exportPurchaseOrders(@RequestBody PurchaseOrderQueryRequest request,
                                     HttpServletResponse response) {
        excelExportService.exportPurchaseOrders(request, response);
    }

    @Log(module = "销售报表", description = "导出销售订单")
    @PostMapping("/sale-orders")
    public void exportSaleOrders(@RequestBody SaleOrderQueryRequest request,
                                 HttpServletResponse response) {
        excelExportService.exportSaleOrders(request, response);
    }

    @Log(module = "库存报表", description = "导出库存")
    @PostMapping("/inventory")
    public void exportInventory(@RequestBody InventoryQueryRequest request,
                                HttpServletResponse response) {
        excelExportService.exportInventory(request, response);
    }
}