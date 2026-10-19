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
 * 购物车明细表
 * @TableName cart_items
 */
@TableName(value ="cart_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItems {
    /**
     * 购物车明细ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 购物车ID
     */
    private Long cart_id;

    /**
     * 商品ID
     */
    private Long product_id;

    /**
     * SKU ID
     */
    private Long sku_id;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 是否选中：0否、1是
     */
    private Integer selected;

    /**
     * 
     */
    private LocalDateTime created_at;

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
        CartItems other = (CartItems) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCart_id() == null ? other.getCart_id() == null : this.getCart_id().equals(other.getCart_id()))
            && (this.getProduct_id() == null ? other.getProduct_id() == null : this.getProduct_id().equals(other.getProduct_id()))
            && (this.getSku_id() == null ? other.getSku_id() == null : this.getSku_id().equals(other.getSku_id()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getSelected() == null ? other.getSelected() == null : this.getSelected().equals(other.getSelected()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCart_id() == null) ? 0 : getCart_id().hashCode());
        result = prime * result + ((getProduct_id() == null) ? 0 : getProduct_id().hashCode());
        result = prime * result + ((getSku_id() == null) ? 0 : getSku_id().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        result = prime * result + ((getSelected() == null) ? 0 : getSelected().hashCode());
        result = prime * result + ((getCreated_at() == null) ? 0 : getCreated_at().hashCode());
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
        sb.append(", cart_id=").append(cart_id);
        sb.append(", product_id=").append(product_id);
        sb.append(", sku_id=").append(sku_id);
        sb.append(", quantity=").append(quantity);
        sb.append(", selected=").append(selected);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append("]");
        return sb.toString();
    }
}