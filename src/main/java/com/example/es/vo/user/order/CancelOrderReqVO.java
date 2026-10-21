package com.example.es.vo.user.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "取消订单请求")
public class CancelOrderReqVO {

    @Schema(description = "取消原因", required = true)
    @NotBlank(message = "取消原因不能为空")
    private String cancelReason;
}
