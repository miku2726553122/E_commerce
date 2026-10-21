package com.example.es.controller.user;

import com.example.es.service.CartsService;
import com.example.es.service.UserAddressesService;
import com.example.es.utils.Response;
import com.example.es.vo.user.AddCartItemReqVO;
import com.example.es.vo.user.UpdateCartItemReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "普通用户接口", description = "购物车与收货地址相关操作")
public class ShoppingCartandDeliveryAddressController {

    @Autowired
    private CartsService cartsService;

    @Autowired
    private UserAddressesService userAddressesService;

    /**
     * 查询购物车
     */
    @GetMapping("/cart")
    @Operation(summary = "查询购物车", description = "查询当前登录用户的购物车信息，包含购物车明细和选中商品总金额")
    public Response getCart() {
        return cartsService.getCart();
    }

    /**
     * 加入购物车
     */
    @PostMapping("/cart/items")
    @Operation(summary = "加入购物车", description = "将指定SKU商品加入购物车，角色权限：USER")
    public Response addToCart(@RequestBody AddCartItemReqVO reqVO) {
        return cartsService.addToCart(reqVO);
    }

    /**
     * 修改购物车明细
     */
    @PutMapping("/cart/items/{id}")
    @Operation(summary = "修改购物车明细", description = "修改购物车明细的数量或选中状态，角色权限：USER")
    public Response updateCartItem(@PathVariable Long id, @RequestBody UpdateCartItemReqVO reqVO) {
        return cartsService.updateCartItem(id, reqVO);
    }

    /**
     * 删除购物车明细
     */
    @DeleteMapping("/cart/items/{id}")
    @Operation(summary = "删除购物车明细", description = "删除指定购物车明细，角色权限：USER")
    public Response deleteCartItem(@PathVariable Long id) {
        return cartsService.deleteCartItem(id);
    }

    /**
     * 查询收货地址
     */
    @GetMapping("/addresses")
    @Operation(summary = "查询收货地址", description = "查询当前登录用户的所有收货地址，角色权限：USER")
    public Response getAddresses() {
        return userAddressesService.getUserAddresses();
    }

}
