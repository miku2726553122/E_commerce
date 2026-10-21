package com.example.es.vo.merchant.shopproduct;//发布商品响应

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "发布商品响应")
public class CreateMerchantProductRspVO {
    private Long id;
    private Long merchantId;
    private Integer auditStatus;
    private Integer saleStatus;
}
