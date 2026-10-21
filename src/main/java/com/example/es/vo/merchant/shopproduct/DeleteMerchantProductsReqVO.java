package com.example.es.vo.merchant.shopproduct;//删除商品

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "删除商品")
public class DeleteMerchantProductsReqVO {
    private Long id;
}