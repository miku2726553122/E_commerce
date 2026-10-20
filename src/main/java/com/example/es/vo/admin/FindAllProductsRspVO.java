package com.example.es.vo.admin;


import com.example.es.entity.Products;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindAllProductsRspVO {
    private List<Products> records;   // 或者 List<FindAllProductsItemVO>
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
}
