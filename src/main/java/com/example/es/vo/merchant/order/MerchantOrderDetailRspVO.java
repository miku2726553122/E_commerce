package com.example.es.vo.merchant.order;

import com.example.es.entity.OrderItems;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商家订单详情响应")
public class MerchantOrderDetailRspVO {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNumber;

    @Schema(description = "订单状态")
    private String orderStatus;

    @Schema(description = "配送状态")
    private String deliveryStatus;

    @Schema(description = "本商家的订单明细")
    private List<OrderItems> items;

    @Schema(description = "本商家商品总额")
    private BigDecimal merchantAmount;

    @Schema(description = "下单时间")
    private LocalDateTime createdAt;

    @Schema(description = "发货时间")
    private LocalDateTime shippedAt;
}
