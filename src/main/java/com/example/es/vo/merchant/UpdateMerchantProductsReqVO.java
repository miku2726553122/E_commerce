package com.example.es.vo.merchant;//修改商品

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
    @Schema(description = "商品封面URL，空字符串表示移除封面，不传则保留原图")
    private String mainImage;
    private String description;
    private String detailContent;
    private BigDecimal basePrice;
}
