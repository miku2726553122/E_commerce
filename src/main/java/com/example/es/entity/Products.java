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
 * 商品表
 * @TableName products
 */
@TableName(value ="products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Products {
    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商家用户ID
     */
    private Long merchant_id;

    /**
     * 分类ID
     */
    private Long category_id;

    /**
     * 商品名称
     */
    private String product_name;

    /**
     * 商品编码
     */
    private String product_code;

    /**
     * 商品主图
     */
    private String main_image;

    /**
     * 商品简介
     */
    private String description;

    /**
     * 商品详情
     */
    private String detail_content;

    /**
     * 商品基础价格
     */
    private BigDecimal base_price;

    /**
     * 销售状态：0下架、1上架
     */
    private Integer sale_status;

    /**
     * 审核状态：0待审核、1审核通过、2审核拒绝
     */
    private Integer audit_status;

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
        Products other = (Products) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMerchant_id() == null ? other.getMerchant_id() == null : this.getMerchant_id().equals(other.getMerchant_id()))
            && (this.getCategory_id() == null ? other.getCategory_id() == null : this.getCategory_id().equals(other.getCategory_id()))
            && (this.getProduct_name() == null ? other.getProduct_name() == null : this.getProduct_name().equals(other.getProduct_name()))
            && (this.getProduct_code() == null ? other.getProduct_code() == null : this.getProduct_code().equals(other.getProduct_code()))
            && (this.getMain_image() == null ? other.getMain_image() == null : this.getMain_image().equals(other.getMain_image()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getDetail_content() == null ? other.getDetail_content() == null : this.getDetail_content().equals(other.getDetail_content()))
            && (this.getBase_price() == null ? other.getBase_price() == null : this.getBase_price().equals(other.getBase_price()))
            && (this.getSale_status() == null ? other.getSale_status() == null : this.getSale_status().equals(other.getSale_status()))
            && (this.getAudit_status() == null ? other.getAudit_status() == null : this.getAudit_status().equals(other.getAudit_status()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMerchant_id() == null) ? 0 : getMerchant_id().hashCode());
        result = prime * result + ((getCategory_id() == null) ? 0 : getCategory_id().hashCode());
        result = prime * result + ((getProduct_name() == null) ? 0 : getProduct_name().hashCode());
        result = prime * result + ((getProduct_code() == null) ? 0 : getProduct_code().hashCode());
        result = prime * result + ((getMain_image() == null) ? 0 : getMain_image().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getDetail_content() == null) ? 0 : getDetail_content().hashCode());
        result = prime * result + ((getBase_price() == null) ? 0 : getBase_price().hashCode());
        result = prime * result + ((getSale_status() == null) ? 0 : getSale_status().hashCode());
        result = prime * result + ((getAudit_status() == null) ? 0 : getAudit_status().hashCode());
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
        sb.append(", merchant_id=").append(merchant_id);
        sb.append(", category_id=").append(category_id);
        sb.append(", product_name=").append(product_name);
        sb.append(", product_code=").append(product_code);
        sb.append(", main_image=").append(main_image);
        sb.append(", description=").append(description);
        sb.append(", detail_content=").append(detail_content);
        sb.append(", base_price=").append(base_price);
        sb.append(", sale_status=").append(sale_status);
        sb.append(", audit_status=").append(audit_status);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append("]");
        return sb.toString();
    }
}