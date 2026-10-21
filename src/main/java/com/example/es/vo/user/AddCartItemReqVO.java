package com.example.es.vo.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCartItemReqVO {
    /**
     * SKU ID
     */
    @NotNull(message = "SKU ID不能为空")
    private Long skuId;

    /**
     * 数量，大于0
     */
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;
}
