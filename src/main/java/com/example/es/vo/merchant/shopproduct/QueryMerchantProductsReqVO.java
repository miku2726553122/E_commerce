package com.example.es.vo.merchant.shopproduct;//查询商品详情

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "查询商品详情")
public class QueryMerchantProductsReqVO {
    private Long id;
}