package com.swalllow_erp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swalllow_erp.entity.PurchaseOrder;
import com.swalllow_erp.entity.PurchaseOrderDetail;
import com.swalllow_erp.entity.Product;
import com.swalllow_erp.mapper.PurchaseOrderDetailMapper;
import com.swalllow_erp.mapper.PurchaseOrderMapper;
import com.swalllow_erp.service.ProductService;
import com.swalllow_erp.service.PurchaseOrderDetailService;
import com.swalllow_erp.service.PurchaseOrderService;
import com.swalllow_erp.dto.request.PurchaseOrderCreateRequest;
import com.swalllow_erp.dto.request.PurchaseOrderQueryRequest;
import com.swalllow_erp.dto.request.PurchaseOrderStatusRequest;
import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
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
import java.util.stream.Collectors;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 22:18
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder>
        implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderDetailService detailService;
    @Autowired
    private PurchaseOrderDetailMapper  detailMapper;
    @Autowired
    private ProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseOrder createOrder(PurchaseOrderCreateRequest request, Integer userId) {
        // 1. 生成订单编号
        String orderNo = generateOrderNo();

        // 2. 计算总金额
        BigDecimal totalAmount = request.getDetails().stream()
                .map(d -> d.getPrice().multiply(new BigDecimal(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. 创建订单主表
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNo(orderNo);
        order.setSupplierId(request.getSupplierId());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        order.setRemark(request.getRemark());
        order.setCreateBy(userId);
        save(order);

        // 4. 创建订单明细
        List<PurchaseOrderDetail> details = new ArrayList<>();
        for (PurchaseOrderCreateRequest.OrderDetailRequest dto : request.getDetails()) {
            PurchaseOrderDetail detail = new PurchaseOrderDetail();
            detail.setOrderId(order.getId());
            detail.setProductId(dto.getProductId());
            detail.setQuantity(dto.getQuantity());
            detail.setPrice(dto.getPrice());
            detail.setAmount(dto.getPrice().multiply(new BigDecimal(dto.getQuantity())));
            detail.setReceivedQuantity(0);
            details.add(detail);
        }

        // ✅ 使用 Service 的 saveBatch() 批量插入
        detailService.saveBatch(details);

        return order;
    }

    @Override
    public PageInfo<PurchaseOrder> queryPage(PurchaseOrderQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
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
        List<PurchaseOrder> list = list(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public PurchaseOrder getOrderWithDetails(Integer orderId) {
        PurchaseOrder order = getById(orderId);
        if (order == null) {
            return null;
        }
        // 查询明细
        List<PurchaseOrderDetail> details = detailMapper.selectByOrderId(orderId);
        order.setDetails(details);
        // TODO: 补充商品名称、供应商名称等关联信息
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Integer orderId, PurchaseOrderStatusRequest request, Integer userId) {
        PurchaseOrder order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("采购订单不存在");
        }
        // 状态流转校验
        Integer currentStatus = order.getStatus();
        Integer targetStatus = request.getStatus();
        if (!isValidStatusTransition(currentStatus, targetStatus)) {
            throw new RuntimeException("状态流转不合法");
        }
        order.setStatus(targetStatus);
        order.setUpdateBy(userId);
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Integer orderId, Integer userId) {
        PurchaseOrder order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("采购订单不存在");
        }
        // 只有草稿和已审核的订单可以取消
        if (order.getStatus() != 0 && order.getStatus() != 1) {
            throw new RuntimeException("当前状态不允许取消");
        }
        order.setStatus(3);  // 已取消
        order.setUpdateBy(userId);
        updateById(order);
    }

    @Override
    public String generateOrderNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 查询今天的订单数量
        LambdaQueryWrapper<PurchaseOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(PurchaseOrder::getOrderNo, "PO" + date)
                .orderByDesc(PurchaseOrder::getOrderNo)
                .last("LIMIT 1");
        PurchaseOrder lastOrder = getOne(wrapper);
        int seq = 1;
        if (lastOrder != null) {
            String lastNo = lastOrder.getOrderNo();
            String seqStr = lastNo.substring(lastNo.length() - 4);
            seq = Integer.parseInt(seqStr) + 1;
        }
        return "PO" + date + String.format("%04d", seq);
    }

    /**
     * 校验状态流转是否合法
     */
    private boolean isValidStatusTransition(Integer current, Integer target) {
        // 0草稿 → 1已审核 → 2已入库 → 3已取消（任意状态都可取消）
        if (target == 3) return true;  // 可以取消
        if (current == 0 && target == 1) return true;   // 草稿→审核
        if (current == 1 && target == 2) return true;   // 审核→入库
        return false;
    }
}