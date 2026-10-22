package com.example.es.vo.merchant;

import com.example.es.entity.ProductSkus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "商家商品详情响应")
public class MerchantProductDetailRspVO {

    private Long id;

    private String productName;

    private BigDecimal basePrice;

    private List<ProductSkus> skus;
}
