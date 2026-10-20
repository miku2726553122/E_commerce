package com.example.es.controller.admin;

import com.example.es.service.OrdersService;
import com.example.es.service.ProductsService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.FindAllProductsReqVO;
import com.example.es.vo.admin.FindAndUpdateReviewProductReqVO;
import com.example.es.vo.admin.FindPageByOrdersReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "商品审核与订单管理", description = "商品审核与订单管理相关接口")
public class ProductReviewandOrderManagementController {

    @Autowired
    private ProductsService productsService;

    @Autowired
    private OrdersService ordersService;

    /**
     * 获取所有商品
     * @param findAllProductsReqVO
     * @return
     */
    @PostMapping("/products")
    @Operation(summary = "获取所有商品", description = "获取所有商品列表")
    public Response getAllProducts(@RequestBody FindAllProductsReqVO findAllProductsReqVO) {
        return productsService.findAllProducts(findAllProductsReqVO);
    }

    /**
     * 审核商品
     * @param id
     * @param findAndUpdateReviewProductReqVO
     * @return
     */

    @PutMapping("/products/{id}/audit")
    @Operation(summary = "审核商品", description = "审核商品")
    public Response reviewProduct(@PathVariable Long id, @RequestBody FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO){
        return productsService.reviewProduct(id, findAndUpdateReviewProductReqVO);
    }
    /**
     * 更新商品上架状态
     * @param id
     * @param findAndUpdateReviewProductReqVO
     * @return
     */

    @PutMapping("/products/{id}/status")
    @Operation(summary = "更新商品上架状态", description = "更新商品上架状态")
    public Response updareProductShelf(@PathVariable Long id, @RequestBody FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO){
        return productsService.updareProductShelf(id, findAndUpdateReviewProductReqVO);

    }


    /**
     * 分页查询订单
     * @param findPageByOrdersReqVO
     * @return
     */
    @PostMapping("/orders")
    @Operation(summary = "分页查询订单", description = "分页查询订单列表")
    public Response findPageByOrders(@RequestBody FindPageByOrdersReqVO findPageByOrdersReqVO){
        return ordersService.findPageByOrders(findPageByOrdersReqVO);
    }

    /**
     * 根据id查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/orders/{id}")
    @Operation(summary = "根据id查询订单详情", description = "根据id查询订单详情")
    public Response findOrdersById(@PathVariable Long id){
        return ordersService.findOrdersById(id);
    }


    public Response updateOrderStatus(){
        return null;
    }
}
