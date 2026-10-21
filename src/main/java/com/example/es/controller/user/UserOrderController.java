package com.example.es.controller.user;

import com.example.es.service.OrdersService;
import com.example.es.utils.Response;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.user.order.CancelOrderReqVO;
import com.example.es.vo.user.order.CreateOrderReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户-订单管理")
@RestController
@RequestMapping("/user/orders")
public class UserOrderController {

    private final OrdersService ordersService;

    public UserOrderController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @Operation(summary = "创建订单",
              description = "POST /user/orders")
    @PostMapping
    public Response createOrder(@Valid @RequestBody CreateOrderReqVO reqVO) {
        return ordersService.createUserOrder(
            SecurityUtils.getCurrentUserId(),
            reqVO
        );
    }

    @Operation(summary = "查询订单列表",
              description = "GET /user/orders")
    @GetMapping
    public Response getOrders() {
        return ordersService.getUserOrders(
            SecurityUtils.getCurrentUserId()
        );
    }

    @Operation(summary = "查询订单详情",
              description = "GET /user/orders/{id}")
    @GetMapping("/{id}")
    public Response getOrderDetail(@PathVariable Long id) {
        return ordersService.getUserOrderDetail(
            SecurityUtils.getCurrentUserId(),
            id
        );
    }

    @Operation(summary = "取消订单",
              description = "PUT /user/orders/{id}/cancel")
    @PutMapping("/{id}/cancel")
    public Response cancelOrder(
            @PathVariable Long id,
            @Valid @RequestBody CancelOrderReqVO reqVO) {
        return ordersService.cancelUserOrder(
            SecurityUtils.getCurrentUserId(),
            id,
            reqVO
        );
    }

    @Operation(summary = "确认收货",
              description = "PUT /user/orders/{id}/confirm")
    @PutMapping("/{id}/confirm")
    public Response confirmReceipt(@PathVariable Long id) {
        return ordersService.confirmReceipt(
            SecurityUtils.getCurrentUserId(),
            id
        );
    }
}
