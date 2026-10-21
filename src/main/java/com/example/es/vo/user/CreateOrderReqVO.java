package com.example.es.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderReqVO {
    /**
     * 收货地址ID
     */
    private Long addressId;

    /**
     * 商品列表
     */
    private List<OrderItemReqVO> items;

    /**
     * 买家备注
     */
    private String userRemark;
}
