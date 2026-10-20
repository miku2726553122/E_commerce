package com.example.es.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Parameter(description = "商家ID")
    private Long merchantId;

    @Parameter(description = "审核状态")
    private Integer auditStatus;

    @Parameter(description = "上架状态")
    private Integer saleStatus;

    @Parameter(description = "页码")
    private Integer pageNum;

    @Parameter(description = "每页条数")
    private Integer pageSize;
}
