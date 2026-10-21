package com.example.es.controller.user;

import com.example.es.auth.CurrentUserProvider;
import com.example.es.service.OrdersService;
import com.example.es.utils.Response;
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
    private final CurrentUserProvider currentUserProvider;

    public UserOrderController(OrdersService ordersService,
                               CurrentUserProvider currentUserProvider) {
        this.ordersService = ordersService;
        this.currentUserProvider = currentUserProvider;
    }

    @Operation(summary = "创建订单",
              description = "POST /user/orders")
    @PostMapping
    public Response createOrder(@Valid @RequestBody CreateOrderReqVO reqVO) {
        return ordersService.createUserOrder(
            currentUserProvider.getUserId(),
            reqVO
        );
    }

    @Operation(summary = "查询订单列表",
              description = "GET /user/orders")
    @GetMapping
    public Response getOrders() {
        return ordersService.getUserOrders(
            currentUserProvider.getUserId()
        );
    }

    @Operation(summary = "查询订单详情",
              description = "GET /user/orders/{id}")
    @GetMapping("/{id}")
    public Response getOrderDetail(@PathVariable Long id) {
        return ordersService.getUserOrderDetail(
            currentUserProvider.getUserId(),
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
            currentUserProvider.getUserId(),
            id,
            reqVO
        );
    }

    @Operation(summary = "确认收货",
              description = "PUT /user/orders/{id}/confirm")
    @PutMapping("/{id}/confirm")
    public Response confirmReceipt(@PathVariable Long id) {
        return ordersService.confirmReceipt(
            currentUserProvider.getUserId(),
            id
        );
    }
}
