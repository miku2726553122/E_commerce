package com.example.es.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商品分类表
 * @TableName categories
 */
@TableName(value ="categories")
@Data
public class Categories {
    /**
     * 分类ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父级分类ID，0表示一级分类
     */
    private Long parent_id;

    /**
     * 分类名称
     */
    private String category_name;

    /**
     * 分类图片
     */
    private String category_image;

    /**
     * 排序值
     */
    private Integer sort_order;

    /**
     * 状态：0禁用、1启用
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
        Categories other = (Categories) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getParent_id() == null ? other.getParent_id() == null : this.getParent_id().equals(other.getParent_id()))
            && (this.getCategory_name() == null ? other.getCategory_name() == null : this.getCategory_name().equals(other.getCategory_name()))
            && (this.getCategory_image() == null ? other.getCategory_image() == null : this.getCategory_image().equals(other.getCategory_image()))
            && (this.getSort_order() == null ? other.getSort_order() == null : this.getSort_order().equals(other.getSort_order()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getParent_id() == null) ? 0 : getParent_id().hashCode());
        result = prime * result + ((getCategory_name() == null) ? 0 : getCategory_name().hashCode());
        result = prime * result + ((getCategory_image() == null) ? 0 : getCategory_image().hashCode());
        result = prime * result + ((getSort_order() == null) ? 0 : getSort_order().hashCode());
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
        sb.append(", parent_id=").append(parent_id);
        sb.append(", category_name=").append(category_name);
        sb.append(", category_image=").append(category_image);
        sb.append(", sort_order=").append(sort_order);
        sb.append(", status=").append(status);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append("]");
        return sb.toString();
    }
}