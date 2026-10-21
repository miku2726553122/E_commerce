package com.example.es.vo.user.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "创建订单请求")
public class CreateOrderReqVO {

    @Schema(description = "购物车商品项列表", required = true)
    @NotEmpty(message = "购物车商品项不能为空")
    private List<OrderItemReq> items;

    @Schema(description = "收货地址ID", required = true)
    @NotNull(message = "收货地址ID不能为空")
    private Long addressId;

    @Schema(description = "买家备注")
    private String userRemark;

    @Data
    @Schema(description = "订单商品项")
    public static class OrderItemReq {

        @Schema(description = "SKU ID", required = true)
        @NotNull(message = "SKU ID不能为空")
        private Long skuId;

        @Schema(description = "购买数量", required = true)
        @NotNull(message = "购买数量不能为空")
        private Integer quantity;
    }
}
