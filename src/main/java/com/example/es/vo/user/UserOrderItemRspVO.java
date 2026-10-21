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
public class UserOrderItemRspVO {
    /**
     * 明细ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * SKU名称
     */
    private String skuName;

    /**
     * 商品图片
     */
    private String productImage;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 小计金额
     */
    private BigDecimal totalAmount;
}
