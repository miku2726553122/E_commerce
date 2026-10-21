package com.example.es.vo.merchant.shopproduct;

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
@Schema(description = "商家商品分页响应")
public class PageMerchantProductsRspVO {

    private List<Products> records;

    private Long total;

    private Integer pageNum;

    private Integer pageSize;
}
