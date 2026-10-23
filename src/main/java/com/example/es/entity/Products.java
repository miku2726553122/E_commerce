package com.example.es.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
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
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private Integer categoryStatus;

    @TableField(exist = false)
    private String categoryParentName;

    /**
     * 商品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 商品编码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 商品主图
     */
    @TableField("main_image")
    private String mainImage;

    /**
     * 商品简介
     */
    private String description;

    /**
     * 商品详情
     */
    @TableField("detail_content")
    private String detailContent;

    /**
     * 商品基础价格
     */
    @TableField("base_price")
    private BigDecimal basePrice;

    /**
     * 销售状态：0下架、1上架
     */
    @TableField("sale_status")
    private Integer saleStatus;

    /**
     * 审核状态：0待审核、1审核通过、2审核拒绝
     */
    @TableField("audit_status")
    private Integer auditStatus;

    /**
     * 审核备注
     */
    @TableField("audit_remark")
    private String auditRemark;

    /**
     * 版本号（乐观锁）
     */
    @Version
    @TableField("version")
    private Integer version;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
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
        Products other = (Products) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMerchantId() == null ? other.getMerchantId() == null : this.getMerchantId().equals(other.getMerchantId()))
            && (this.getCategoryId() == null ? other.getCategoryId() == null : this.getCategoryId().equals(other.getCategoryId()))
            && (this.getProductName() == null ? other.getProductName() == null : this.getProductName().equals(other.getProductName()))
            && (this.getProductCode() == null ? other.getProductCode() == null : this.getProductCode().equals(other.getProductCode()))
            && (this.getMainImage() == null ? other.getMainImage() == null : this.getMainImage().equals(other.getMainImage()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getDetailContent() == null ? other.getDetailContent() == null : this.getDetailContent().equals(other.getDetailContent()))
            && (this.getBasePrice() == null ? other.getBasePrice() == null : this.getBasePrice().equals(other.getBasePrice()))
            && (this.getSaleStatus() == null ? other.getSaleStatus() == null : this.getSaleStatus().equals(other.getSaleStatus()))
            && (this.getAuditStatus() == null ? other.getAuditStatus() == null : this.getAuditStatus().equals(other.getAuditStatus()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMerchantId() == null) ? 0 : getMerchantId().hashCode());
        result = prime * result + ((getCategoryId() == null) ? 0 : getCategoryId().hashCode());
        result = prime * result + ((getProductName() == null) ? 0 : getProductName().hashCode());
        result = prime * result + ((getProductCode() == null) ? 0 : getProductCode().hashCode());
        result = prime * result + ((getMainImage() == null) ? 0 : getMainImage().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getDetailContent() == null) ? 0 : getDetailContent().hashCode());
        result = prime * result + ((getBasePrice() == null) ? 0 : getBasePrice().hashCode());
        result = prime * result + ((getSaleStatus() == null) ? 0 : getSaleStatus().hashCode());
        result = prime * result + ((getAuditStatus() == null) ? 0 : getAuditStatus().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
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
        sb.append(", merchantId=").append(merchantId);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", productName=").append(productName);
        sb.append(", productCode=").append(productCode);
        sb.append(", mainImage=").append(mainImage);
        sb.append(", description=").append(description);
        sb.append(", detailContent=").append(detailContent);
        sb.append(", basePrice=").append(basePrice);
        sb.append(", saleStatus=").append(saleStatus);
        sb.append(", auditStatus=").append(auditStatus);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append("]");
        return sb.toString();
    }
}
