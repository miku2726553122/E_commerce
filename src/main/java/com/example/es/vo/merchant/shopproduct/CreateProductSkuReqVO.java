package com.example.es.vo.merchant.shopproduct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "添加商品SKU请求")
public class CreateProductSkuReqVO {

    @Schema(description = "SKU名称", example = "红色-L码")
    @NotBlank(message = "SKU名称不能为空")
    private String skuName;

    @Schema(description = "SKU编码", example = "SKU202410210001")
    @NotBlank(message = "SKU编码不能为空")
    private String skuCode;

    @Schema(description = "价格", example = "199.00")
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;

    @Schema(description = "库存数量", example = "100")
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能为负数")
    private Integer stock;

    @Schema(description = "SKU图片URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "规格属性JSON", example = "{\"颜色\":\"红色\",\"尺码\":\"L\"}")
    private String attributes;
}
