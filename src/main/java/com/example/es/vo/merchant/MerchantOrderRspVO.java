package com.example.es.vo.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商家订单列表响应")
public class MerchantOrderRspVO {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNumber;

    @Schema(description = "订单状态")
    private String orderStatus;

    @Schema(description = "配送状态")
    private String deliveryStatus;

    private String globalDeliveryStatus;
    private String paymentStatus;
    private Boolean canShip;

    @Schema(description = "本商家商品总额")
    private BigDecimal merchantAmount;

    @Schema(description = "下单时间")
    private LocalDateTime createdAt;
}
