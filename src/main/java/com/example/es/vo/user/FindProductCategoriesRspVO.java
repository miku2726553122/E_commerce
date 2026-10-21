package com.example.es.vo.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindProductCategoriesRspVO {
    private Long id;
    private String categoryName;
    private List<FindChildrenProductCategoriesRspVO> children;
}
