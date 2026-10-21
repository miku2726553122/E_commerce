package com.example.es.vo.user.order;

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
@Schema(description = "用户订单列表响应")
public class UserOrderRspVO {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "商品总金额")
    private BigDecimal totalAmount;

    @Schema(description = "运费")
    private BigDecimal freightAmount;

    @Schema(description = "应付金额")
    private BigDecimal payableAmount;

    @Schema(description = "订单状态")
    private String orderStatus;

    @Schema(description = "支付状态")
    private String paymentStatus;

    @Schema(description = "配送状态")
    private String deliveryStatus;

    @Schema(description = "下单时间")
    private LocalDateTime createdAt;
}
