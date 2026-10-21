package com.example.es.controller.merchant;

import com.example.es.auth.CurrentUserProvider;
import com.example.es.service.ProductsService;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.shopproduct.CreateMerchantProductReqVO;
import com.example.es.vo.merchant.shopproduct.PageMerchantProductsReqVO;
import com.example.es.vo.merchant.shopproduct.UpdateMerchantProductStatusReqVO;
import com.example.es.vo.merchant.shopproduct.UpdateMerchantProductsReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final CurrentUserProvider currentUserProvider;
    public MerchantProductController(ProductsService productsService
            , CurrentUserProvider currentUserProvider) {
        this.productsService = productsService;
        this.currentUserProvider = currentUserProvider;
    }

    @Operation(summary = "发布商品", description = "Post /merchant/products")
    @PostMapping
    public Response createProduct(
            @RequestBody CreateMerchantProductReqVO reqVO) {

        Long merchantId = currentUserProvider.getMerchantId();

        return productsService.createMerchantProduct(merchantId, reqVO);
    }

    @Operation(summary = "分页查询自己的商品", description = "GET /merchant/products")
    @GetMapping
    public Response pageProducts(
            @ModelAttribute PageMerchantProductsReqVO reqVO) {
        return productsService.pageMerchantProducts(
                currentUserProvider.getMerchantId(), reqVO);
    }

    @Operation(summary = "查询自己的商品详情",
            description = "GET /merchant/products/{id}")
    @GetMapping("/{id}")
    public Response getProduct(@PathVariable Long id) {
        return productsService.getMerchantProduct(
                currentUserProvider.getMerchantId(), id);
    }

    @Operation(summary = "修改商品",
            description = "PUT /merchant/products/{id}")
    @PutMapping("/{id}")
    public Response updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateMerchantProductsReqVO reqVO) {
        return productsService.updateMerchantProduct(
                currentUserProvider.getMerchantId(), id, reqVO);
    }

    @Operation(summary = "删除商品",
            description = "DELETE /merchant/products/{id}")
    @DeleteMapping("/{id}")
    public Response deleteProduct(@PathVariable Long id) {
        return productsService.deleteMerchantProduct(
                currentUserProvider.getMerchantId(), id);
    }

    @Operation(summary = "商品上架或下架",
            description = "PUT /merchant/products/{id}/status")
    @PutMapping("/{id}/status")
    public Response updateProductStatus(
            @PathVariable Long id,
            @RequestBody UpdateMerchantProductStatusReqVO reqVO) {
        return productsService.updateMerchantProductStatus(
                currentUserProvider.getMerchantId(), id, reqVO);
    }
}
