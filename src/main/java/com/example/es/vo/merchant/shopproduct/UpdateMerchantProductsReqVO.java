package com.example.es.vo.merchant.shopproduct;//修改商品

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "修改商品")
public class UpdateMerchantProductsReqVO {
    private String productName;
    private String description;
    private String detailContent;
    private BigDecimal basePrice;
}
