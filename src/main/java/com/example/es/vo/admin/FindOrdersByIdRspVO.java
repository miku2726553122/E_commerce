package com.example.es.vo.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindOrdersByIdRspVO {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal payableAmount;
    private String orderStatus;
    private List<FindOrdersItemsByIdRspVO> items;
}
