package com.example.es.controller.merchant;

import com.example.es.auth.CurrentUserProvider;
import com.example.es.service.OrdersService;
import com.example.es.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商家-订单管理")
@RestController
@RequestMapping("/merchant/orders")
public class MerchantOrderController {

    private final OrdersService ordersService;
    private final CurrentUserProvider currentUserProvider;

    public MerchantOrderController(OrdersService ordersService,
                                   CurrentUserProvider currentUserProvider) {
        this.ordersService = ordersService;
        this.currentUserProvider = currentUserProvider;
    }

    @Operation(summary = "查询关联订单",
            description = "GET /merchant/orders")
    @GetMapping
    public Response getOrders() {
        return ordersService.getMerchantOrders(
                currentUserProvider.getMerchantId()
        );
    }

    @Operation(summary = "查询订单详情",
            description = "GET /merchant/orders/{id}")
    @GetMapping("/{id}")
    public Response getOrderDetail(@PathVariable Long id) {
        return ordersService.getMerchantOrderDetail(
                currentUserProvider.getMerchantId(),
                id
        );
    }

    @Operation(summary = "确认发货",
            description = "PUT /merchant/orders/{id}/ship")
    @PutMapping("/{id}/ship")
    public Response confirmShipment(@PathVariable Long id) {
        return ordersService.confirmShipment(
                currentUserProvider.getMerchantId(),
                id
        );
    }
}
