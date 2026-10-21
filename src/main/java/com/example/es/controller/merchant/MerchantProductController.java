package com.example.es.controller.merchant;

import com.example.es.service.ProductSkusService;
import com.example.es.service.ProductsService;
import com.example.es.utils.Response;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.merchant.shopproduct.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "商家-商品")
@RestController
@RequestMapping("/merchant/products")
public class MerchantProductController {
    private final ProductsService productsService;
    private final ProductSkusService productSkusService;

    public MerchantProductController(ProductsService productsService,
                                     ProductSkusService productSkusService) {
        this.productsService = productsService;
        this.productSkusService = productSkusService;
    }


    @Operation(summary = "发布商品", description = "Post /merchant/products")
    @PostMapping
    public Response createProduct(
            @RequestBody CreateMerchantProductReqVO reqVO) {

        Long merchantId = SecurityUtils.getCurrentUserId();

        return productsService.createMerchantProduct(merchantId, reqVO);
    }

    @Operation(summary = "分页查询自己的商品", description = "GET /merchant/products")
    @GetMapping
    public Response pageProducts(
            @ModelAttribute PageMerchantProductsReqVO reqVO) {
        return productsService.pageMerchantProducts(
                SecurityUtils.getCurrentUserId(), reqVO);
    }

    @Operation(summary = "查询自己的商品详情",
            description = "GET /merchant/products/{id}")
    @GetMapping("/{id}")
    public Response getProduct(@PathVariable Long id) {
        return productsService.getMerchantProduct(
                SecurityUtils.getCurrentUserId(), id);
    }

    @Operation(summary = "修改商品",
            description = "PUT /merchant/products/{id}")
    @PutMapping("/{id}")
    public Response updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateMerchantProductsReqVO reqVO) {
        return productsService.updateMerchantProduct(
                SecurityUtils.getCurrentUserId(), id, reqVO);
    }

    @Operation(summary = "删除商品",
            description = "DELETE /merchant/products/{id}")
    @DeleteMapping("/{id}")
    public Response deleteProduct(@PathVariable Long id) {
        return productsService.deleteMerchantProduct(
                SecurityUtils.getCurrentUserId(), id);
    }

    @Operation(summary = "商品上架或下架",
            description = "PUT /merchant/products/{id}/status")
    @PutMapping("/{id}/status")
    public Response updateProductStatus(
            @PathVariable Long id,
            @RequestBody UpdateMerchantProductStatusReqVO reqVO) {
        return productsService.updateMerchantProductStatus(
                SecurityUtils.getCurrentUserId(), id, reqVO);
    }

    @Operation(summary = "新增商品SKU",
            description = "POST /merchant/products/{productId}/skus")
    @PostMapping("/{productId}/skus")
    public Response createSku(
            @PathVariable Long productId,
            @RequestBody @Valid CreateProductSkuReqVO reqVO) {

        Long merchantId = SecurityUtils.getCurrentUserId();
        return productSkusService.createProductSku(merchantId, productId, reqVO);
    }

    @Operation(summary = "修改商品SKU",
            description = "PUT /merchant/products/{id}/skus/{skuId}")
    @PutMapping("/{id}/skus/{skuId}")
    public Response updateSku(
            @PathVariable Long id,
            @PathVariable Long skuId,
            @RequestBody @Valid UpdateProductSkuReqVO reqVO) {

        Long merchantId = SecurityUtils.getCurrentUserId();
        return productSkusService.updateProductSku(merchantId, id, skuId, reqVO);
    }

    @Operation(summary = "删除商品SKU",
            description = "DELETE /merchant/products/{id}/skus/{skuId}")
    @DeleteMapping("/{id}/skus/{skuId}")
    public Response deleteSku(
            @PathVariable Long id,
            @PathVariable Long skuId) {

        Long merchantId = SecurityUtils.getCurrentUserId();
        return productSkusService.deleteProductSku(merchantId, id, skuId);
    }


}
