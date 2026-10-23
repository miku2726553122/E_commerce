package com.example.es.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserOrderRspVO {
    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 收货地址
     */
    private String receiverAddress;

    /**
     * 应付金额
     */
    private BigDecimal payableAmount;

    /**
     * 订单状态
     */
    private String orderStatus;

    private String nickname;
    private String receiverName;
    private String receiverPhone;
    private String paymentStatus;
    private String deliveryStatus;
    private String cancelReason;
    private String userRemark;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;

    /**
     * 订单明细
     */
    private java.util.List<UserOrderItemRspVO> items;
}
