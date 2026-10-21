package com.example.es.vo.user;

import com.example.es.entity.Products;
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
@Schema(description = "用户查询商品响应")
public class FindUserProductsRspVO {

    @Schema(description = "商品列表")
    private List<Products> records;

    @Schema(description = "总条数")
    private Long total;

    @Schema(description = "页码")
    private Integer pageNum;

    @Schema(description = "每页条数")
    private Integer pageSize;
}
