package com.example.es.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品库存表
 * @TableName inventories
 */
@TableName(value ="inventories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventories {
    /**
     * 库存ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * SKU ID
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * 实际库存数量
     */
    @TableField("stock_quantity")
    private Integer stockQuantity;

    /**
     * 锁定库存数量
     */
    @TableField("locked_quantity")
    private Integer lockedQuantity;

    /**
     * 库存预警数量
     */
    @TableField("warning_quantity")
    private Integer warningQuantity;

    /**
     *
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Inventories other = (Inventories) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSkuId() == null ? other.getSkuId() == null : this.getSkuId().equals(other.getSkuId()))
            && (this.getStockQuantity() == null ? other.getStockQuantity() == null : this.getStockQuantity().equals(other.getStockQuantity()))
            && (this.getLockedQuantity() == null ? other.getLockedQuantity() == null : this.getLockedQuantity().equals(other.getLockedQuantity()))
            && (this.getWarningQuantity() == null ? other.getWarningQuantity() == null : this.getWarningQuantity().equals(other.getWarningQuantity()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSkuId() == null) ? 0 : getSkuId().hashCode());
        result = prime * result + ((getStockQuantity() == null) ? 0 : getStockQuantity().hashCode());
        result = prime * result + ((getLockedQuantity() == null) ? 0 : getLockedQuantity().hashCode());
        result = prime * result + ((getWarningQuantity() == null) ? 0 : getWarningQuantity().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", skuId=").append(skuId);
        sb.append(", stockQuantity=").append(stockQuantity);
        sb.append(", lockedQuantity=").append(lockedQuantity);
        sb.append(", warningQuantity=").append(warningQuantity);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}