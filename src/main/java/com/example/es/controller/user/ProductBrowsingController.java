package com.example.es.controller.user;


import com.example.es.service.CategoriesService;
import com.example.es.service.ProductsService;
import com.example.es.utils.Response;
import com.example.es.vo.user.FindUserProductsReqVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class ProductBrowsingController {

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private ProductsService productsService;

    @GetMapping("/categories")
    @Operation(summary = "获取商品类别", description = "获取商品类别列表")
    public Response FindProductCategories(){
        return categoriesService.findAllCategories();
    }

    @GetMapping("/products")
    @Operation(summary = "分页查询商品", description = "分页查询已上架且审核通过的商品")
    public Response FindUserProducts(FindUserProductsReqVO reqVO){
        return productsService.findUserProducts(reqVO);
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "查询商品详情", description = "根据ID查询商品详情")
    public Response FindProductDetail(@PathVariable Long id){
        return productsService.findProductDetail(id);
    }
}
