package com.example.es.vo.user;

import com.example.es.entity.ProductSkus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "商品详情响应")
public class ProductDetailRspVO {

    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品简介")
    private String description;

    @Schema(description = "主图")
    private String mainImage;

    @Schema(description = "SKU列表")
    private List<ProductSkus> skus;
}
