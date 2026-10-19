package com.example.es.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单明细表
 * @TableName order_items
 */
@TableName(value ="order_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItems {
    /**
     * 订单明细ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long order_id;

    /**
     * 商品ID
     */
    private Long product_id;

    /**
     * SKU ID
     */
    private Long sku_id;

    /**
     * 商品名称快照
     */
    private String product_name;

    /**
     * SKU名称快照
     */
    private String sku_name;

    /**
     * 商品规格快照
     */
    private Object sku_specification;

    /**
     * 商品图片快照
     */
    private String product_image;

    /**
     * 商品成交单价
     */
    private BigDecimal unit_price;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 明细总金额
     */
    private BigDecimal total_amount;

    /**
     * 
     */
    private LocalDateTime created_at;

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
        OrderItems other = (OrderItems) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrder_id() == null ? other.getOrder_id() == null : this.getOrder_id().equals(other.getOrder_id()))
            && (this.getProduct_id() == null ? other.getProduct_id() == null : this.getProduct_id().equals(other.getProduct_id()))
            && (this.getSku_id() == null ? other.getSku_id() == null : this.getSku_id().equals(other.getSku_id()))
            && (this.getProduct_name() == null ? other.getProduct_name() == null : this.getProduct_name().equals(other.getProduct_name()))
            && (this.getSku_name() == null ? other.getSku_name() == null : this.getSku_name().equals(other.getSku_name()))
            && (this.getSku_specification() == null ? other.getSku_specification() == null : this.getSku_specification().equals(other.getSku_specification()))
            && (this.getProduct_image() == null ? other.getProduct_image() == null : this.getProduct_image().equals(other.getProduct_image()))
            && (this.getUnit_price() == null ? other.getUnit_price() == null : this.getUnit_price().equals(other.getUnit_price()))
            && (this.getQuantity() == null ? other.getQuantity() == null : this.getQuantity().equals(other.getQuantity()))
            && (this.getTotal_amount() == null ? other.getTotal_amount() == null : this.getTotal_amount().equals(other.getTotal_amount()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrder_id() == null) ? 0 : getOrder_id().hashCode());
        result = prime * result + ((getProduct_id() == null) ? 0 : getProduct_id().hashCode());
        result = prime * result + ((getSku_id() == null) ? 0 : getSku_id().hashCode());
        result = prime * result + ((getProduct_name() == null) ? 0 : getProduct_name().hashCode());
        result = prime * result + ((getSku_name() == null) ? 0 : getSku_name().hashCode());
        result = prime * result + ((getSku_specification() == null) ? 0 : getSku_specification().hashCode());
        result = prime * result + ((getProduct_image() == null) ? 0 : getProduct_image().hashCode());
        result = prime * result + ((getUnit_price() == null) ? 0 : getUnit_price().hashCode());
        result = prime * result + ((getQuantity() == null) ? 0 : getQuantity().hashCode());
        result = prime * result + ((getTotal_amount() == null) ? 0 : getTotal_amount().hashCode());
        result = prime * result + ((getCreated_at() == null) ? 0 : getCreated_at().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", order_id=").append(order_id);
        sb.append(", product_id=").append(product_id);
        sb.append(", sku_id=").append(sku_id);
        sb.append(", product_name=").append(product_name);
        sb.append(", sku_name=").append(sku_name);
        sb.append(", sku_specification=").append(sku_specification);
        sb.append(", product_image=").append(product_image);
        sb.append(", unit_price=").append(unit_price);
        sb.append(", quantity=").append(quantity);
        sb.append(", total_amount=").append(total_amount);
        sb.append(", created_at=").append(created_at);
        sb.append("]");
        return sb.toString();
    }
}