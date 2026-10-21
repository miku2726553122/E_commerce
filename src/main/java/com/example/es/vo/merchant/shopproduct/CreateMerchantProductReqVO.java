package com.example.es.vo.merchant.shopproduct;//发布商品

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "发布商品")
public class CreateMerchantProductReqVO {
    private Long categoryId;
    private String productName;
    private String productCode;
    private String mainImage;
    private String description;
    private String detailContent;
    private BigDecimal basePrice;
}