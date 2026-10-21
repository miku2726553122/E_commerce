package com.example.es.vo.user.order;

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
@Schema(description = "用户订单详情响应")
public class UserOrderDetailRspVO {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "收货人姓名")
    private String receiverName;

    @Schema(description = "收货电话")
    private String receiverPhone;

    @Schema(description = "收货地址")
    private String receiverAddress;

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

    @Schema(description = "买家备注")
    private String userRemark;

    @Schema(description = "订单明细")
    private List<OrderItems> items;

    @Schema(description = "下单时间")
    private LocalDateTime createdAt;

    @Schema(description = "支付时间")
    private LocalDateTime paidAt;

    @Schema(description = "发货时间")
    private LocalDateTime shippedAt;

    @Schema(description = "完成时间")
    private LocalDateTime completedAt;
}
