package com.swalllow_erp.controller;

import com.alibaba.fastjson2.JSON;
import com.swalllow_erp.common.CommonCodeEnum;
import com.swalllow_erp.common.CommonResult;
import com.swalllow_erp.dto.request.ProductCreateRequest;
import com.swalllow_erp.dto.request.ProductUpdateRequest;
import com.swalllow_erp.entity.Product;
import com.swalllow_erp.entity.ProductListing;
import com.swalllow_erp.service.ProductListingService;
import com.swalllow_erp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Swallow333
 * @Date: 2026/05/25 21:35
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductListingService productListingService;

    @GetMapping
    public CommonResult<List<Product>> list() {
        List<Product> list = productService.list();
        return CommonResult.success(list);
    }

    @GetMapping("/{id}")
    public CommonResult<Product> getById(@PathVariable Integer id) {
        Product product = productService.getById(id);
        if (product == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        return CommonResult.success(product);
    }

    @GetMapping("/sku/{sku}")
    public CommonResult<Product> getBySku(@PathVariable String sku) {
        Product product = productService.findBySku(sku);
        if (product == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        return CommonResult.success(product);
    }

    @GetMapping("/asin/{asin}")
    public CommonResult<List<Product>> getByAsin(@PathVariable String asin) {
        List<Product> products = productService.findByAsin(asin);
        return CommonResult.success(products);
    }

    @PostMapping
    public CommonResult<Void> create(@RequestBody @Valid ProductCreateRequest request) {
        // 检查SKU是否重复
        Product existProduct = productService.findBySku(request.getSku());
        if (existProduct != null) {
            return CommonResult.error(CommonCodeEnum.DATA_DUPLICATE);
        }

        // 保存商品主表
        Product product = new Product();
        product.setAsin(request.getAsin());
        product.setSku(request.getSku());
        product.setTitle(request.getTitle());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setPrice(request.getPrice());
        product.setCostPrice(request.getCostPrice());
        product.setStatus(1);

        productService.save(product);

        // 保存Listing详情
        ProductListing listing = new ProductListing();
        listing.setProductId(product.getId());
        if (request.getBulletPoints() != null) {
            listing.setBulletPoints(JSON.toJSONString(request.getBulletPoints()));
        }
        listing.setDescription(request.getDescription());
        listing.setSearchTerms(request.getSearchTerms());

        productListingService.save(listing);

        return CommonResult.success("创建成功", null);
    }

    @PutMapping("/{id}")
    public CommonResult<Void> update(@PathVariable Integer id,
                                     @RequestBody ProductUpdateRequest request) {
        Product product = productService.getById(id);
        if (product == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }

        if (request.getTitle() != null) product.setTitle(request.getTitle());
        if (request.getBrand() != null) product.setBrand(request.getBrand());
        if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getCostPrice() != null) product.setCostPrice(request.getCostPrice());
        if (request.getStatus() != null) product.setStatus(request.getStatus());

        productService.updateById(product);

        // 更新Listing详情
        ProductListing listing = productListingService.findByProductId(id);
        if (listing != null && request.getBulletPoints() != null) {
            listing.setBulletPoints(JSON.toJSONString(request.getBulletPoints()));
            listing.setDescription(request.getDescription());
            listing.setSearchTerms(request.getSearchTerms());
            productListingService.updateById(listing);
        }

        return CommonResult.success("修改成功", null);
    }

    @DeleteMapping("/{id}")
    public CommonResult<Void> delete(@PathVariable Integer id) {
        Product product = productService.getById(id);
        if (product == null) {
            return CommonResult.error(CommonCodeEnum.DATA_NOT_EXIST);
        }
        productService.removeById(id);
        // 级联删除Listing
        ProductListing listing = productListingService.findByProductId(id);
        if (listing != null) {
            productListingService.removeById(listing.getId());
        }
        return CommonResult.success("删除成功", null);
    }
}