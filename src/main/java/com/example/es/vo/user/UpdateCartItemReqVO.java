package com.example.es.vo.user;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemReqVO {
    /**
     * 数量（大于0）
     */
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;

    /**
     * 是否选中
     */
    private Boolean selected;
}
