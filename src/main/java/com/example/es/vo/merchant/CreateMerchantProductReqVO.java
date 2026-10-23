package com.example.es.vo.merchant;//发布商品

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
    @Schema(description = "申请的新分类名称，与 categoryId 二选一")
    private String newCategoryName;
    @Schema(description = "新分类的父级分类，0 表示一级分类")
    private Long categoryParentId;
    private String productName;
    private String productCode;
    private String mainImage;
    private String description;
    private String detailContent;
    private BigDecimal basePrice;
}
