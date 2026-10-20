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
@Schema(description = "分页查询订单请求参数")
public class FindPageByOrdersReqVO {
    @Parameter(description = "订单编号")
    private String orderNo;

    @Parameter(description = "买家ID")
    private Long userId;

    @Parameter(description = "订单状态")
    private String orderStatus;

    @Parameter(description = "页码")
    private Integer pageNum;

    @Parameter(description = "每页条数")
    private Integer pageSize;
}
