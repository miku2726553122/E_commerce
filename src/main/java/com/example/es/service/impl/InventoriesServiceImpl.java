package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Inventories;
import com.example.es.entity.ProductSkus;
import com.example.es.entity.Products;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.mapper.InventoriesMapper;
import com.example.es.mapper.ProductSkusMapper;
import com.example.es.mapper.ProductsMapper;
import com.example.es.service.InventoriesService;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.InventoryRspVO;
import com.example.es.vo.merchant.UpdateInventoryReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoriesServiceImpl extends ServiceImpl<InventoriesMapper, Inventories>
        implements InventoriesService {

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private ProductSkusMapper productSkusMapper;

    @Override
    public Response getMerchantInventories(Long merchantId) {
        if (merchantId == null) {
            return Response.fail(ResponseCodeEnum.UNAUTHORIZED);
        }

        // 查询商家所有商品
        List<Products> products = productsMapper.selectList(
                Wrappers.<Products>lambdaQuery()
                        .eq(Products::getMerchantId, merchantId)
        );

        if (products.isEmpty()) {
            return Response.success(new ArrayList<>());
        }

        List<Long> productIds = products.stream()
                .map(Products::getId)
                .collect(Collectors.toList());

        // 查询这些商品的所有 SKU
        List<ProductSkus> skus = productSkusMapper.selectList(
                Wrappers.<ProductSkus>lambdaQuery()
                        .in(ProductSkus::getProductId, productIds)
        );

        // 查询这些 SKU 的库存
        List<Long> skuIds = skus.stream()
                .map(ProductSkus::getId)
                .collect(Collectors.toList());

        if (skuIds.isEmpty()) {
            return Response.success(new ArrayList<>());
        }

        List<Inventories> inventories = this.list(
                Wrappers.<Inventories>lambdaQuery()
                        .in(Inventories::getSkuId, skuIds)
        );

        // 组装响应数据
        List<InventoryRspVO> result = inventories.stream()
                .map(inventory -> {
                    ProductSkus sku = skus.stream()
                            .filter(s -> s.getId().equals(inventory.getSkuId()))
                            .findFirst()
                            .orElse(null);

                    if (sku == null) return null;

                    Products product = products.stream()
                            .filter(p -> p.getId().equals(sku.getProductId()))
                            .findFirst()
                            .orElse(null);

                    if (product == null) return null;

                    return InventoryRspVO.builder()
                            .id(inventory.getId())
                            .skuId(inventory.getSkuId())
                            .skuCode(sku.getSkuCode())
                            .skuName(sku.getSkuName())
                            .productName(product.getProductName())
                            .stockQuantity(inventory.getStockQuantity())
                            .lockedQuantity(inventory.getLockedQuantity())
                            .availableStock(inventory.getStockQuantity() - inventory.getLockedQuantity())
                            .warningQuantity(inventory.getWarningQuantity())
                            .build();
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());

        return Response.success(result);
    }

    @Override
    public Response updateInventory(Long merchantId, Long skuId, UpdateInventoryReqVO reqVO) {
        if (merchantId == null || skuId == null || reqVO == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 查询 SKU
        ProductSkus sku = productSkusMapper.selectById(skuId);
        if (sku == null) {
            return Response.fail("SKU不存在");
        }

        // 验证 SKU 对应的商品是否属于当前商家
        Products product = productsMapper.selectById(sku.getProductId());
        if (product == null || !merchantId.equals(product.getMerchantId())) {
            return Response.fail("无权操作该SKU的库存");
        }

        // 查询库存记录
        Inventories inventory = this.getOne(
                Wrappers.<Inventories>lambdaQuery()
                        .eq(Inventories::getSkuId, skuId)
        );

        if (inventory == null) {
            return Response.fail("库存记录不存在");
        }

        // 使用乐观锁重试更新
        int maxRetries = 3;
        int retryCount = 0;
        boolean updated = false;
        while (retryCount < maxRetries && !updated) {
            // 重新查询最新版本
            inventory = this.getById(inventory.getId());
            if (inventory == null) {
                return Response.fail("库存记录不存在");
            }
            
            Inventories updateInventory = Inventories.builder()
                    .id(inventory.getId())
                    .stockQuantity(reqVO.getStockQuantity())
                    .warningQuantity(reqVO.getWarningQuantity())
                    .version(inventory.getVersion()) // 带上版本号
                    .build();

            boolean success = this.updateById(updateInventory);
            if (success) {
                updated = true;
            } else {
                retryCount++;
            }
        }
        
        if (!updated) {
            return Response.fail("更新失败，请重试");
        }
        return Response.success(true);
    }
}
