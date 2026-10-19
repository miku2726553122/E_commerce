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
    private Long sku_id;

    /**
     * 实际库存数量
     */
    private Integer stock_quantity;

    /**
     * 锁定库存数量
     */
    private Integer locked_quantity;

    /**
     * 库存预警数量
     */
    private Integer warning_quantity;

    /**
     * 
     */
    private LocalDateTime updated_at;

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
            && (this.getSku_id() == null ? other.getSku_id() == null : this.getSku_id().equals(other.getSku_id()))
            && (this.getStock_quantity() == null ? other.getStock_quantity() == null : this.getStock_quantity().equals(other.getStock_quantity()))
            && (this.getLocked_quantity() == null ? other.getLocked_quantity() == null : this.getLocked_quantity().equals(other.getLocked_quantity()))
            && (this.getWarning_quantity() == null ? other.getWarning_quantity() == null : this.getWarning_quantity().equals(other.getWarning_quantity()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSku_id() == null) ? 0 : getSku_id().hashCode());
        result = prime * result + ((getStock_quantity() == null) ? 0 : getStock_quantity().hashCode());
        result = prime * result + ((getLocked_quantity() == null) ? 0 : getLocked_quantity().hashCode());
        result = prime * result + ((getWarning_quantity() == null) ? 0 : getWarning_quantity().hashCode());
        result = prime * result + ((getUpdated_at() == null) ? 0 : getUpdated_at().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sku_id=").append(sku_id);
        sb.append(", stock_quantity=").append(stock_quantity);
        sb.append(", locked_quantity=").append(locked_quantity);
        sb.append(", warning_quantity=").append(warning_quantity);
        sb.append(", updated_at=").append(updated_at);
        sb.append("]");
        return sb.toString();
    }
}