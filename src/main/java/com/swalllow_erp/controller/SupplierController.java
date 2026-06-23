package com.swalllow_erp.controller;


import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.request.SupplierCreateRequest;
import com.swalllow_erp.dto.request.SupplierQueryRequest;
import com.swalllow_erp.dto.request.SupplierUpdateRequest;
import com.swalllow_erp.entity.Supplier;
import com.swalllow_erp.service.SupplierService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/06/22 1:42
 */

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 分页查询供应商
     */
    @PostMapping("/page")
    public CommonResult<PageInfo<Supplier>> queryPage(@RequestBody SupplierQueryRequest request) {
        PageInfo<Supplier> page = supplierService.queryPage(request);
        return CommonResult.success(page);
    }

    /**
     * 查询所有启用的供应商（下拉框用）
     */
    @GetMapping("/enabled")
    public CommonResult<List<Supplier>> getEnabledList() {
        List<Supplier> list = supplierService.getEnabledList();
        return CommonResult.success(list);
    }

    /**
     * 根据ID查询供应商
     */
    @GetMapping("/{id}")
    public CommonResult<Supplier> getById(@PathVariable Integer id) {
        Supplier supplier = supplierService.getById(id);
        if (supplier == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        return CommonResult.success(supplier);
    }

    /**
     * 根据编码查询供应商
     */
    @GetMapping("/code/{code}")
    public CommonResult<Supplier> getByCode(@PathVariable String code) {
        Supplier supplier = supplierService.findByCode(code);
        if (supplier == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        return CommonResult.success(supplier);
    }

    /**
     * 新增供应商
     */
    @PostMapping
    public CommonResult<Void> create(@RequestBody @Valid SupplierCreateRequest request) {
        // 检查编码是否重复
        if (supplierService.isCodeDuplicate(request.getCode(), null)) {
            return CommonResult.error(CommonCodeEnum.DATA_DUPLICATE);
        }

        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(request, supplier);
        supplier.setStatus(1);

        supplierService.save(supplier);
        return CommonResult.success("创建成功", null);
    }

    /**
     * 修改供应商
     */
    @PutMapping("/{id}")
    public CommonResult<Void> update(@PathVariable Integer id,
                                     @RequestBody SupplierUpdateRequest request) {
        Supplier supplier = supplierService.getById(id);
        if (supplier == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }

        // ✅ 编码不允许修改，不处理 code
        supplier.setName(request.getName());
        supplier.setContact(request.getContact());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setTaxNo(request.getTaxNo());
        supplier.setBankName(request.getBankName());
        supplier.setBankAccount(request.getBankAccount());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setLeadDays(request.getLeadDays());
        supplier.setRemark(request.getRemark());

        if (request.getStatus() != null) {
            supplier.setStatus(request.getStatus());
        }

        supplierService.updateById(supplier);
        return CommonResult.success("修改成功", null);
    }

    /**
     * 删除供应商
     */
    @DeleteMapping("/{id}")
    public CommonResult<Void> delete(@PathVariable Integer id) {
        Supplier supplier = supplierService.getById(id);
        if (supplier == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }

        // TODO: 检查是否有采购订单关联此供应商
        // 如果有，不能删除，只能禁用

        supplierService.removeById(id);
        return CommonResult.success("删除成功", null);
    }

    /**
     * 启用/禁用供应商
     */
    @PutMapping("/{id}/status")
    public CommonResult<Void> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        Supplier supplier = supplierService.getById(id);
        if (supplier == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        supplier.setStatus(status);
        supplierService.updateById(supplier);
        return CommonResult.success(status == 1 ? "启用成功" : "禁用成功", null);
    }
}