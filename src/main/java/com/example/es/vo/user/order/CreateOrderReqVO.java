package com.example.es.vo.user.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "创建订单请求")
public class CreateOrderReqVO {

    @Schema(description = "购物车商品项列表", required = true)
    @NotEmpty(message = "购物车商品项不能为空")
    @Valid
    private List<@NotNull @Valid OrderItemReq> items;

    @Schema(description = "收货地址ID", required = true)
    @NotNull(message = "收货地址ID不能为空")
    private Long addressId;

    @Schema(description = "买家备注")
    @Size(max = 500, message = "买家备注不能超过500字")
    private String userRemark;

    @Data
    @Schema(description = "订单商品项")
    public static class OrderItemReq {

        @Schema(description = "SKU ID", required = true)
        @NotNull(message = "SKU ID不能为空")
        private Long skuId;

        @Schema(description = "购买数量", required = true)
        @NotNull(message = "购买数量不能为空")
        @Positive(message = "购买数量必须大于0")
        private Integer quantity;
    }
}
