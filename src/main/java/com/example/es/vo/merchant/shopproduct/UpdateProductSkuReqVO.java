package com.example.es.vo.merchant.shopproduct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "修改商品SKU请求")
public class UpdateProductSkuReqVO {

    @Schema(description = "SKU名称")
    private String skuName;

    @Schema(description = "价格")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    @Schema(description = "SKU图片URL")
    private String imageUrl;

    @Schema(description = "规格属性JSON")
    private String attributes;
}