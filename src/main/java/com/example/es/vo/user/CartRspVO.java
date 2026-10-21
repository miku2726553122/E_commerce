package com.example.es.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRspVO {
    /**
     * 购物车ID
     */
    private Long id;

    /**
     * 购物车明细
     */
    private List<CartItemRspVO> items;

    /**
     * 选中商品总金额
     */
    private BigDecimal totalAmount;
}
