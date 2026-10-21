package com.example.es.controller.user;

import com.example.es.service.OrdersService;
import com.example.es.utils.Response;
import com.example.es.vo.user.CancelOrderReqVO;
import com.example.es.vo.user.CreateOrderReqVO;
import com.example.es.vo.user.UserOrderPageReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/orders")
@Tag(name = "普通用户接口", description = "用户订单相关操作")
public class UserOrderController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 提交订单
     */
    @PostMapping
    @Operation(summary = "提交订单", description = "用户提交订单，角色权限：USER")
    public Response createOrder(@RequestBody CreateOrderReqVO reqVO) {
        return ordersService.createOrder(reqVO);
    }

    /**
     * 查询我的订单
     */
    @GetMapping
    @Operation(summary = "查询我的订单", description = "分页查询当前用户的订单列表，角色权限：USER")
    public Response getUserOrders(UserOrderPageReqVO reqVO) {
        return ordersService.getUserOrders(reqVO);
    }

    /**
     * 查询订单详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询订单详情", description = "查询指定订单的详细信息，角色权限：USER")
    public Response getUserOrderById(@PathVariable Long id) {
        return ordersService.getUserOrderById(id);
    }

    /**
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消订单", description = "取消待付款的订单，角色权限：USER")
    public Response cancelOrder(@PathVariable Long id, @RequestBody CancelOrderReqVO reqVO) {
        return ordersService.cancelOrder(id, reqVO);
    }

    /**
     * 确认收货
     */
    @PutMapping("/{id}/receive")
    @Operation(summary = "确认收货", description = "确认已发货的订单已收货，角色权限：USER")
    public Response receiveOrder(@PathVariable Long id) {
        return ordersService.receiveOrder(id);
    }
}
