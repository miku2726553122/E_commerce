package com.example.es.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "用户查询商品请求参数")
public class FindUserProductsReqVO {

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "商品关键词")
    private String keyword;

    @Schema(description = "页码")
    private Integer pageNum;

    @Schema(description = "每页条数")
    private Integer pageSize;
}
