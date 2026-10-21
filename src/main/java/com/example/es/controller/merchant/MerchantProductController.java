package com.example.es.controller.merchant;

import com.example.es.auth.CurrentUserProvider;
import com.example.es.service.ProductsService;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.shopproduct.CreateMerchantProductReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
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

}
