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
public class CartItemRspVO {
    /**
     * 购物车明细ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图
     */
    private String mainImage;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * SKU名称
     */
    private String skuName;

    /**
     * SKU图片
     */
    private String skuImageUrl;

    /**
     * 销售价
     */
    private BigDecimal salePrice;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 是否选中：0否、1是
     */
    private Integer selected;
}
