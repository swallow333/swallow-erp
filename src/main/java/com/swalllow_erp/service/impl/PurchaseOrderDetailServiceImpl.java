package com.swalllow_erp.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swalllow_erp.entity.PurchaseOrderDetail;
import com.swalllow_erp.mapper.PurchaseOrderDetailMapper;
import com.swalllow_erp.service.PurchaseOrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @Author: Swallow333
 * @Date: 2026/06/24 2:40
 */

@Service
public class PurchaseOrderDetailServiceImpl
        extends ServiceImpl<PurchaseOrderDetailMapper, PurchaseOrderDetail>
        implements PurchaseOrderDetailService {
}