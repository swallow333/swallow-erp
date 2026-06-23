package com.swalllow_erp.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.swalllow_erp.common.Log;
import com.swalllow_erp.dto.export.InventoryExportDTO;
import com.swalllow_erp.dto.export.PurchaseOrderExportDTO;
import com.swalllow_erp.dto.export.SaleOrderExportDTO;
import com.swalllow_erp.dto.request.InventoryQueryRequest;
import com.swalllow_erp.dto.request.PurchaseOrderQueryRequest;
import com.swalllow_erp.dto.request.SaleOrderQueryRequest;
import com.swalllow_erp.entity.Inventory;
import com.swalllow_erp.entity.PurchaseOrder;
import com.swalllow_erp.entity.SaleOrder;
import com.swalllow_erp.entity.Supplier;
import com.swalllow_erp.mapper.InventoryMapper;
import com.swalllow_erp.mapper.PurchaseOrderMapper;
import com.swalllow_erp.mapper.SaleOrderMapper;
import com.swalllow_erp.mapper.SupplierMapper;
import com.swalllow_erp.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 1:31
 */
@Service
public class ExcelExportServiceImpl implements ExcelExportService {
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private SaleOrderMapper saleOrderMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Log(module = "采购报表", description = "导出采购订单")
    public void exportPurchaseOrders(PurchaseOrderQueryRequest request, HttpServletResponse response) {
        // 1. 查询数据
        LambdaQueryWrapper<PurchaseOrder> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getOrderNo())) {
            wrapper.like(PurchaseOrder::getOrderNo, request.getOrderNo());
        }
        if (request.getSupplierId() != null) {
            wrapper.eq(PurchaseOrder::getSupplierId, request.getSupplierId());
        }
        if (request.getStatus() != null) {
            wrapper.eq(PurchaseOrder::getStatus, request.getStatus());
        }
        wrapper.orderByDesc(PurchaseOrder::getCreateTime);
        List<PurchaseOrder> orders = purchaseOrderMapper.selectList(wrapper);
        // 2. 转换为导出 DTO
        List<PurchaseOrderExportDTO> exportList = new ArrayList<>();
        for (PurchaseOrder order : orders) {
            PurchaseOrderExportDTO dto = new PurchaseOrderExportDTO();
            dto.setOrderNo(order.getOrderNo());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setRemark(order.getRemark());
            dto.setCreateTime(order.getCreateTime().format(DATE_FORMATTER));
            dto.setStatusName(getPurchaseStatusName(order.getStatus()));
            Supplier supplier = supplierMapper.selectById(order.getSupplierId());
            dto.setSupplierName(supplier != null ? supplier.getName() : "-");
            exportList.add(dto);
        }
        // 3. 导出 Excel
        exportExcel(response, exportList, PurchaseOrderExportDTO.class, "采购订单");
    }

    @Override
    @Log(module = "销售报表", description = "导出销售订单")
    public void exportSaleOrders(SaleOrderQueryRequest request, HttpServletResponse response) {
        LambdaQueryWrapper<SaleOrder> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getOrderNo())) {
            wrapper.like(SaleOrder::getOrderNo, request.getOrderNo());
        }
        if (StringUtils.hasText(request.getCustomerName())) {
            wrapper.like(SaleOrder::getCustomerName, request.getCustomerName());
        }
        if (request.getStatus() != null) {
            wrapper.eq(SaleOrder::getStatus, request.getStatus());
        }
        wrapper.orderByDesc(SaleOrder::getCreateTime);
        List<SaleOrder> orders = saleOrderMapper.selectList(wrapper);
        List<SaleOrderExportDTO> exportList = new ArrayList<>();
        for (SaleOrder order : orders) {
            SaleOrderExportDTO dto = new SaleOrderExportDTO();
            dto.setOrderNo(order.getOrderNo());
            dto.setCustomerName(order.getCustomerName());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatusName(getSaleStatusName(order.getStatus()));
            dto.setCreateTime(order.getCreateTime().format(DATE_FORMATTER));
            exportList.add(dto);
        }
        exportExcel(response, exportList, SaleOrderExportDTO.class, "销售订单");
    }

    @Override
    @Log(module = "库存报表", description = "导出库存")
    public void exportInventory(InventoryQueryRequest request, HttpServletResponse response) {
        // 查询库存数据
        List<Inventory> inventoryList = inventoryMapper.selectListWithProduct();
        List<InventoryExportDTO> exportList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            InventoryExportDTO dto = new InventoryExportDTO();
            dto.setProductSku(inventory.getProductSku());
            dto.setProductName(inventory.getProductName());
            dto.setQuantity(inventory.getQuantity());
            dto.setLockedQuantity(inventory.getLockedQuantity());
            dto.setAvailableQuantity(inventory.getAvailableQuantity());
            exportList.add(dto);
        }
        exportExcel(response, exportList, InventoryExportDTO.class, "库存报表");
    }

    // ========== 通用导出方法 ==========
    private <T> void exportExcel(HttpServletResponse response, List<T> data,
                                 Class<T> clazz, String sheetName) {
        try {
            String fileName = URLEncoder.encode(
                            sheetName + "_" + LocalDateTime.now().format(
                                    DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
                            StandardCharsets.UTF_8)
                    .replace("+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), clazz)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet(sheetName)
                    .doWrite(data);
        } catch (IOException e) {
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    // ========== 状态名称转换 ==========
    private String getPurchaseStatusName(Integer status) {
        if (status == null) return "-";
        switch (status) {
            case 0:
                return "草稿";
            case 1:
                return "已审核";
            case 2:
                return "已入库";
            case 3:
                return "已取消";
            default:
                return "-";
        }
    }

    private String getSaleStatusName(Integer status) {
        if (status == null) return "-";
        switch (status) {
            case 0:
                return "草稿";
            case 1:
                return "已审核";
            case 2:
                return "已发货";
            case 3:
                return "已完成";
            case 4:
                return "已取消";
            default:
                return "-";
        }
    }
}