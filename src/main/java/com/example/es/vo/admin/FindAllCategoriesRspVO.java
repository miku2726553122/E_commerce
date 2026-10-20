package com.example.es.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindAllCategoriesRspVO {
    private Long id;

    private Long parentId;

    private String categoryName;

    private Integer status;
}
