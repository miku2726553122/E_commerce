package com.example.es.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    /**
     * 订单明细
     */
    private java.util.List<UserOrderItemRspVO> items;
}
