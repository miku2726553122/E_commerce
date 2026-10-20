package com.example.es.vo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class FindOrdersItemsByIdRspVO {
    /**
     * 订单明细ID
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long productId;

    private FindProductByIdRspVO product;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 商品名称快照
     */
    private String productName;

    /**
     * SKU名称快照
     */
    private String skuName;

    /**
     * 商品规格快照
     */
    private Object skuSpecification;

    /**
     * 商品图片快照
     */
    private String productImage;

    /**
     * 商品成交单价
     */
    private BigDecimal unitPrice;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 明细总金额
     */
    private BigDecimal totalAmount;
}
