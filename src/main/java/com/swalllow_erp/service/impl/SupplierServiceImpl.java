package com.swalllow_erp.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swalllow_erp.entity.Supplier;
import com.swalllow_erp.mapper.SupplierMapper;
import com.swalllow_erp.service.SupplierService;
import com.swalllow_erp.dto.request.SupplierQueryRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;


/**
 * @Author: Swallow333
 * @Date: 2026/06/22 1:42
 */


@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier>
        implements SupplierService {

    @Override
    public Supplier findByCode(String code) {
        return baseMapper.findByCode(code);
    }

    @Override
    public PageInfo<Supplier> queryPage(SupplierQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());

        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();

        // 关键词搜索（名称或编码）
        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.and(w -> w
                    .like(Supplier::getName, request.getKeyword())
                    .or()
                    .like(Supplier::getCode, request.getKeyword())
            );
        }

        // 状态筛选
        if (request.getStatus() != null) {
            wrapper.eq(Supplier::getStatus, request.getStatus());
        }

        wrapper.orderByAsc(Supplier::getCode);

        List<Supplier> list = list(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public List<Supplier> getEnabledList() {
        return baseMapper.selectEnabledList();
    }

    @Override
    public boolean isCodeDuplicate(String code, Integer excludeId) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Supplier::getCode, code);
        if (excludeId != null) {
            wrapper.ne(Supplier::getId, excludeId);
        }
        return count(wrapper) > 0;
    }
}