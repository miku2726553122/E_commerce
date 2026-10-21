package com.example.es.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemRspVO {
    /**
     * 明细ID
     */
    private Long id;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 数量
     */
    private Integer quantity;
}
