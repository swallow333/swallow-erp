package com.swalllow_erp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swalllow_erp.entity.Inventory;
import com.swalllow_erp.entity.StockFlow;
import com.swalllow_erp.dto.request.InventoryQueryRequest;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/23 22:12
 */

public interface InventoryService extends IService<Inventory> {
    /**
     * 分页查询库存列表（含商品信息）
     */
    PageInfo<Inventory> queryPage(InventoryQueryRequest request);
    /**
     * 查询单个商品的库存
     */
    Inventory getByProductId(Integer productId);
    /**
     * 查询库存流水（分页）
     */
    PageInfo<StockFlow> queryFlows(Integer productId, Integer pageNum, Integer pageSize);
    /**
     * 增加库存（入库时调用）
     */
    void increaseStock(Integer productId, Integer quantity, String sourceType, Integer sourceId, String remark);
    /**
     * 扣减库存（出库时调用）
     */
    void decreaseStock(Integer productId, Integer quantity, String sourceType, Integer sourceId, String remark);
    /**
     * 锁定库存（下单时锁定）
     */
    void lockStock(Integer productId, Integer quantity);
    /**
     * 解锁库存（取消订单时解锁）
     */
    void unlockStock(Integer productId, Integer quantity);
}