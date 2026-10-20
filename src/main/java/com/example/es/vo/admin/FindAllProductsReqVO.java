package com.example.es.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "查询所有商品请求参数")
public class FindAllProductsReqVO {
    private Long merchantId;

    private Integer auditStatus;

    private Integer saleStatus;

    private Integer pageNum;

    private Integer pageSize;
}
