package com.example.es.vo.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "修改库存请求")
public class UpdateInventoryReqVO {

    @Schema(description = "库存数量", required = true)
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能为负数")
    private Integer stockQuantity;

    @Schema(description = "预警数量")
    @Min(value = 0, message = "预警数量不能为负数")
    private Integer warningQuantity;
}
