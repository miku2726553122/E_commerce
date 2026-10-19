package com.example.es.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户表
 * @TableName users
 */
@TableName(value ="users")
@Data
public class Users {
    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 登录账号
     */
    private String username;

    /**
     * 密码哈希
     */
    private String password_hash;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 真实姓名
     */
    private String real_name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 商家店铺名称
     */
    private String shop_name;

    /**
     * 商家店铺Logo
     */
    private String shop_logo;

    /**
     * 角色：ADMIN管理员、USER普通用户、MERCHANT商家
     */
    private String role;

    /**
     * 状态：0禁用、1正常
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
        Users other = (Users) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPassword_hash() == null ? other.getPassword_hash() == null : this.getPassword_hash().equals(other.getPassword_hash()))
            && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
            && (this.getReal_name() == null ? other.getReal_name() == null : this.getReal_name().equals(other.getReal_name()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getAvatar() == null ? other.getAvatar() == null : this.getAvatar().equals(other.getAvatar()))
            && (this.getShop_name() == null ? other.getShop_name() == null : this.getShop_name().equals(other.getShop_name()))
            && (this.getShop_logo() == null ? other.getShop_logo() == null : this.getShop_logo().equals(other.getShop_logo()))
            && (this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword_hash() == null) ? 0 : getPassword_hash().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getReal_name() == null) ? 0 : getReal_name().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getShop_name() == null) ? 0 : getShop_name().hashCode());
        result = prime * result + ((getShop_logo() == null) ? 0 : getShop_logo().hashCode());
        result = prime * result + ((getRole() == null) ? 0 : getRole().hashCode());
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
        sb.append(", username=").append(username);
        sb.append(", password_hash=").append(password_hash);
        sb.append(", nickname=").append(nickname);
        sb.append(", real_name=").append(real_name);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", avatar=").append(avatar);
        sb.append(", shop_name=").append(shop_name);
        sb.append(", shop_logo=").append(shop_logo);
        sb.append(", role=").append(role);
        sb.append(", status=").append(status);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append("]");
        return sb.toString();
    }
}