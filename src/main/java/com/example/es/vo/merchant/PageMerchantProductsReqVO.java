package com.example.es.vo.merchant;//分页查询商品

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "分页查询商品")
public class PageMerchantProductsReqVO {
    private Integer saleStatus;
    private Integer auditStatus;
    private Integer pageNum;
    private Integer pageSize;

}