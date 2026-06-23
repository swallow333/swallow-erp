package com.swalllow_erp.service.impl;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.swalllow_erp.dto.export.PurchaseOrderExportDTO;
import com.swalllow_erp.dto.request.PurchaseOrderQueryRequest;
import com.swalllow_erp.entity.PurchaseOrder;
import com.swalllow_erp.entity.Supplier;
import com.swalllow_erp.mapper.PurchaseOrderMapper;
import com.swalllow_erp.mapper.SupplierMapper;
import com.swalllow_erp.service.ExcelExportService;
import com.swalllow_erp.common.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
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
            dto.setStatusName(getStatusName(order.getStatus()));

            // 供应商名称
            Supplier supplier = supplierMapper.selectById(order.getSupplierId());
            dto.setSupplierName(supplier != null ? supplier.getName() : "-");

            exportList.add(dto);
        }

        // 3. 导出 Excel
        try {
            String fileName = URLEncoder.encode("采购订单_" + LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), StandardCharsets.UTF_8)
                    .replace("+", "%20");

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

            EasyExcel.write(response.getOutputStream(), PurchaseOrderExportDTO.class)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("采购订单")
                    .doWrite(exportList);

        } catch (IOException e) {
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    // ========== 其他导出方法类似 ==========

    private String getStatusName(Integer status) {
        if (status == null) return "-";
        switch (status) {
            case 0: return "草稿";
            case 1: return "已审核";
            case 2: return "已入库";
            case 3: return "已取消";
            default: return "-";
        }
    }
}