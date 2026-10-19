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
 * 商品SKU表
 * @TableName product_skus
 */
@TableName(value ="product_skus")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSkus {
    /**
     * SKU ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long product_id;

    /**
     * SKU编码
     */
    private String sku_code;

    /**
     * SKU名称
     */
    private String sku_name;

    /**
     * 商品规格
     */
    private Object specification;

    /**
     * 原价
     */
    private BigDecimal original_price;

    /**
     * 销售价
     */
    private BigDecimal sale_price;

    /**
     * SKU图片
     */
    private String image_url;

    /**
     * 状态：0停用、1启用
     */
    private Integer status;

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
        ProductSkus other = (ProductSkus) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProduct_id() == null ? other.getProduct_id() == null : this.getProduct_id().equals(other.getProduct_id()))
            && (this.getSku_code() == null ? other.getSku_code() == null : this.getSku_code().equals(other.getSku_code()))
            && (this.getSku_name() == null ? other.getSku_name() == null : this.getSku_name().equals(other.getSku_name()))
            && (this.getSpecification() == null ? other.getSpecification() == null : this.getSpecification().equals(other.getSpecification()))
            && (this.getOriginal_price() == null ? other.getOriginal_price() == null : this.getOriginal_price().equals(other.getOriginal_price()))
            && (this.getSale_price() == null ? other.getSale_price() == null : this.getSale_price().equals(other.getSale_price()))
            && (this.getImage_url() == null ? other.getImage_url() == null : this.getImage_url().equals(other.getImage_url()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProduct_id() == null) ? 0 : getProduct_id().hashCode());
        result = prime * result + ((getSku_code() == null) ? 0 : getSku_code().hashCode());
        result = prime * result + ((getSku_name() == null) ? 0 : getSku_name().hashCode());
        result = prime * result + ((getSpecification() == null) ? 0 : getSpecification().hashCode());
        result = prime * result + ((getOriginal_price() == null) ? 0 : getOriginal_price().hashCode());
        result = prime * result + ((getSale_price() == null) ? 0 : getSale_price().hashCode());
        result = prime * result + ((getImage_url() == null) ? 0 : getImage_url().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
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
        sb.append(", product_id=").append(product_id);
        sb.append(", sku_code=").append(sku_code);
        sb.append(", sku_name=").append(sku_name);
        sb.append(", specification=").append(specification);
        sb.append(", original_price=").append(original_price);
        sb.append(", sale_price=").append(sale_price);
        sb.append(", image_url=").append(image_url);
        sb.append(", status=").append(status);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append("]");
        return sb.toString();
    }
}