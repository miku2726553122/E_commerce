package com.example.es.vo.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "库存信息响应")
public class InventoryRspVO {

    @Schema(description = "库存ID")
    private Long id;

    @Schema(description = "SKU ID")
    private Long skuId;

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "SKU名称")
    private String skuName;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "库存数量")
    private Integer stockQuantity;

    @Schema(description = "锁定数量")
    private Integer lockedQuantity;

    @Schema(description = "可用库存")
    private Integer availableStock;

    @Schema(description = "预警数量")
    private Integer warningQuantity;
}
