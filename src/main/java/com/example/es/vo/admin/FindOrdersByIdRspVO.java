package com.example.es.vo.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindOrdersByIdRspVO {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal payableAmount;
    private String orderStatus;
    private String paymentStatus;
    private String deliveryStatus;
    private String nickname;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String userRemark;
    private String cancelReason;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private List<FindOrdersItemsByIdRspVO> items;
}
