package com.example.es.controller.admin;

import com.example.es.service.ProductsService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.FindAllProductsReqVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "商品审核与订单管理", description = "商品审核与订单管理相关接口")
public class ProductReviewandOrderManagementController {

    @Autowired
    private ProductsService productsService;

    @GetMapping ("/products")
    @Tag(name = "获取所有商品", description = "获取所有商品列表")
    public Response getAllProducts(FindAllProductsReqVO findAllProductsReqVO) {
        return productsService.findAllProducts(findAllProductsReqVO);
    }
}
