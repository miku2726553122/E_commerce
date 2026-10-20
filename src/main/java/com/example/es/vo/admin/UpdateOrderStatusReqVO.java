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
@Schema(description = "修改订单状态")
public class UpdateOrderStatusReqVO {
    @Schema(description = "订单状态")
    private String orderStatus;

    @Schema(description = "处理说明")
    private String remark;
}
