package com.swalllow_erp.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swalllow_erp.entity.*;
import com.swalllow_erp.exception.BusinessException;
import com.swalllow_erp.mapper.*;
import com.swalllow_erp.service.StockInService;
import com.swalllow_erp.dto.request.StockInCreateRequest;
import com.swalllow_erp.dto.request.StockInQueryRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:00
 */

@Service
public class StockInServiceImpl extends ServiceImpl<StockInMapper, StockIn>
        implements StockInService {

    @Autowired
    private StockInDetailMapper stockInDetailMapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private StockFlowMapper stockFlowMapper;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private PurchaseOrderDetailMapper purchaseOrderDetailMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StockIn createStockIn(StockInCreateRequest request, Integer userId) {
        // 1. 查询采购订单
        PurchaseOrder order = purchaseOrderMapper.selectById(request.getPurchaseOrderId());
        if (order == null) {
            throw new RuntimeException("采购订单不存在");
        }
        if (order.getStatus() != 1) {
            throw new RuntimeException("采购订单状态不正确，只有已审核的订单可以入库");
        }

        // 2. 生成入库单号
        String inNo = generateInNo();

        // 3. 计算总数
        int totalQuantity = request.getDetails().stream()
                .mapToInt(StockInCreateRequest.StockInDetailRequest::getQuantity)
                .sum();
        BigDecimal totalAmount = request.getDetails().stream()
                .map(d -> d.getPrice().multiply(new BigDecimal(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 创建入库单主表
        StockIn stockIn = new StockIn();
        stockIn.setInNo(inNo);
        stockIn.setPurchaseOrderId(request.getPurchaseOrderId());
        stockIn.setSupplierId(order.getSupplierId());
        stockIn.setInDate(LocalDateTime.now());
        stockIn.setTotalQuantity(totalQuantity);
        stockIn.setTotalAmount(totalAmount);
        stockIn.setStatus(1);
        stockIn.setRemark(request.getRemark());
        stockIn.setCreateBy(userId);

        save(stockIn);

        // 5. 创建入库明细 + 更新库存 + 记录流水
        for (StockInCreateRequest.StockInDetailRequest dto : request.getDetails()) {
            // 5.1 保存入库明细
            StockInDetail detail = new StockInDetail();
            detail.setStockInId(stockIn.getId());
            detail.setProductId(dto.getProductId());
            detail.setQuantity(dto.getQuantity());
            detail.setPrice(dto.getPrice());
            detail.setAmount(dto.getPrice().multiply(new BigDecimal(dto.getQuantity())));
            stockInDetailMapper.insert(detail);

            // ✅ 5.1.1 更新采购订单明细的已入库数量
            // 首先根据订单 ID 和商品 ID 查询对应的订单明细
            List<PurchaseOrderDetail> orderDetails = purchaseOrderDetailMapper
                    .selectByOrderAndProduct(request.getPurchaseOrderId(), dto.getProductId());
            if (orderDetails.isEmpty()) {
                throw new BusinessException("采购订单中未找到该商品明细");
            }
            PurchaseOrderDetail orderDetail = orderDetails.get(0);
            // 累加已入库数量
            int newReceived = orderDetail.getReceivedQuantity() + dto.getQuantity();
            if (newReceived > orderDetail.getQuantity()) {
                throw new BusinessException("入库数量超过采购数量，请检查");
            }
            purchaseOrderDetailMapper.increaseReceivedQuantity(orderDetail.getId(), dto.getQuantity());

            // 5.2 更新库存
            Inventory inventory = inventoryMapper.selectByProductId(dto.getProductId());
            if (inventory == null) {
                // 商品没有库存记录，创建
                inventory = new Inventory();
                inventory.setProductId(dto.getProductId());
                inventory.setQuantity(0);
                inventory.setLockedQuantity(0);
                inventory.setAvailableQuantity(0);
                inventoryMapper.insert(inventory);
            }

            int beforeQuantity = inventory.getQuantity();
            int afterQuantity = beforeQuantity + dto.getQuantity();

            inventory.setQuantity(afterQuantity);
            inventory.setAvailableQuantity(afterQuantity - inventory.getLockedQuantity());
            inventoryMapper.updateById(inventory);

            // 5.3 记录库存流水
            StockFlow flow = new StockFlow();
            flow.setProductId(dto.getProductId());
            flow.setFlowType(1);  // 入库
            flow.setChangeQuantity(dto.getQuantity());
            flow.setBeforeQuantity(beforeQuantity);
            flow.setAfterQuantity(afterQuantity);
            flow.setSourceType("PURCHASE_IN");
            flow.setSourceId(stockIn.getId());
            flow.setRemark("采购入库，采购单号：" + order.getOrderNo());
            stockFlowMapper.insert(flow);
        }

        // 6. 更新采购订单状态为已入库
        order.setStatus(2);
        purchaseOrderMapper.updateById(order);

        // 7. 更新采购订单明细的已入库数量

        return stockIn;
    }

    @Override
    public PageInfo<StockIn> queryPage(StockInQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<StockIn> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getInNo())) {
            wrapper.like(StockIn::getInNo, request.getInNo());
        }
        if (request.getSupplierId() != null) {
            wrapper.eq(StockIn::getSupplierId, request.getSupplierId());
        }
        wrapper.orderByDesc(StockIn::getCreateTime);

        List<StockIn> list = list(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public StockIn getStockInWithDetails(Integer stockInId) {
        StockIn stockIn = getById(stockInId);
        if (stockIn == null) {
            return null;
        }

        List<StockInDetail> details = stockInDetailMapper.selectByStockInId(stockInId);
        stockIn.setDetails(details);
        return stockIn;
    }

    @Override
    public String generateInNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LambdaQueryWrapper<StockIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(StockIn::getInNo, "IN" + date)
                .orderByDesc(StockIn::getInNo)
                .last("LIMIT 1");
        StockIn last = getOne(wrapper);

        int seq = 1;
        if (last != null) {
            String lastNo = last.getInNo();
            String seqStr = lastNo.substring(lastNo.length() - 4);
            seq = Integer.parseInt(seqStr) + 1;
        }
        return "IN" + date + String.format("%04d", seq);
    }
}