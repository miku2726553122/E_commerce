package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.ProductSkus;
import com.example.es.service.ProductSkusService;
import com.example.es.mapper.ProductSkusMapper;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.CreateProductSkuReqVO;
import com.example.es.vo.merchant.UpdateProductSkuReqVO;
import org.springframework.stereotype.Service;
import com.example.es.entity.Products;
import com.example.es.entity.Inventories;
import com.example.es.mapper.ProductsMapper;
import com.example.es.service.InventoriesService;
import com.example.es.enums.ResponseCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;


/**
* @author 86159
* @description 针对表【product_skus(商品SKU表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class ProductSkusServiceImpl extends ServiceImpl<ProductSkusMapper, ProductSkus>
    implements ProductSkusService{

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private InventoriesService inventoriesService;

    @Override
    public Response createProductSku(Long merchantId, Long productId, CreateProductSkuReqVO reqVO) {
        // 1. 参数校验
        if (merchantId == null || productId == null || reqVO == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 2. 验证商品归属：查询商品是否存在且属于当前商家
        Products product = productsMapper.selectById(productId);
        if (product == null || !merchantId.equals(product.getMerchantId())) {
            return Response.fail("商品不存在或无权操作");
        }

        // 3. 创建 SKU
        ProductSkus sku = ProductSkus.builder()
                .productId(productId)
                .skuName(reqVO.getSkuName())
                .skuCode(reqVO.getSkuCode())
                .salePrice(reqVO.getPrice())
                .originalPrice(reqVO.getPrice())
                .imageUrl(reqVO.getImageUrl())
                .specification(reqVO.getAttributes())
                .status(1)
                .build();


        boolean saved = this.save(sku);
        if (!saved) {
            return Response.fail("新增SKU失败");
        }

        // 4. 创建对应的库存记录
        Inventories inventory = Inventories.builder()
                .skuId(sku.getId())
                .stockQuantity(reqVO.getStock())
                .lockedQuantity(0)
                .warningQuantity(10)  // 默认预警值
                .build();

        inventoriesService.save(inventory);

        // 5. 返回新增的 SKU ID
        return Response.success(sku.getId());
    }

    @Override
    public Response updateProductSku(Long merchantId, Long productId, Long skuId, UpdateProductSkuReqVO reqVO) {
        // 1. 参数校验
        if (merchantId == null || productId == null || skuId == null || reqVO == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 2. 验证商品归属
        Products product = productsMapper.selectById(productId);
        if (product == null || !merchantId.equals(product.getMerchantId())) {
            return Response.fail("商品不存在或无权操作");
        }

        // 3. 验证 SKU 是否属于该商品
        ProductSkus existingSku = this.getById(skuId);
        if (existingSku == null || !productId.equals(existingSku.getProductId())) {
            return Response.fail("SKU不存在或不属于该商品");
        }

        // 4. 更新 SKU（只更新传了的字段）
        ProductSkus updateSku = ProductSkus.builder()
                .id(skuId)
                .skuName(reqVO.getSkuName())
                .salePrice(reqVO.getPrice())
                .originalPrice(reqVO.getPrice())
                .imageUrl(reqVO.getImageUrl())
                .specification(reqVO.getAttributes())
                .build();

        boolean updated = this.updateById(updateSku);
        return Response.success(updated);
    }

    @Override
    public Response deleteProductSku(Long merchantId, Long productId, Long skuId) {
        // 1. 参数校验
        if (merchantId == null || productId == null || skuId == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 2. 验证商品归属
        Products product = productsMapper.selectById(productId);
        if (product == null || !merchantId.equals(product.getMerchantId())) {
            return Response.fail("商品不存在或无权操作");
        }

        // 3. 验证 SKU 是否属于该商品
        ProductSkus existingSku = this.getById(skuId);
        if (existingSku == null || !productId.equals(existingSku.getProductId())) {
            return Response.fail("SKU不存在或不属于该商品");
        }

        // 4. 删除 SKU 和对应的库存记录
        boolean deleted = this.removeById(skuId);

        if (deleted) {
            // 同时删除库存记录
            inventoriesService.remove(
                    new LambdaQueryWrapper<Inventories>()
                            .eq(Inventories::getSkuId, skuId)
            );
        }

        return Response.success(deleted);
    }


}




