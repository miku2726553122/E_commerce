package com.example.es.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemReqVO {
    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 购买数量
     */
    private Integer quantity;
}
