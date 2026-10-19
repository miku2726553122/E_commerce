package com.example.es.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 订单表
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders {
    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String order_no;

    /**
     * 买家用户ID
     */
    private Long user_id;

    /**
     * 收货人姓名
     */
    private String receiver_name;

    /**
     * 收货电话
     */
    private String receiver_phone;

    /**
     * 收货地址
     */
    private String receiver_address;

    /**
     * 商品总金额
     */
    private BigDecimal total_amount;

    /**
     * 运费
     */
    private BigDecimal freight_amount;

    /**
     * 应付金额
     */
    private BigDecimal payable_amount;

    /**
     * 订单状态：待付款、待发货、已发货、已完成、已取消
     */
    private String order_status;

    /**
     * 支付状态：未支付、已支付
     */
    private String payment_status;

    /**
     * 配送状态：待发货、已发货、已签收
     */
    private String delivery_status;

    /**
     * 买家备注
     */
    private String user_remark;

    /**
     * 取消原因
     */
    private String cancel_reason;

    /**
     * 支付时间
     */
    private LocalDateTime paid_at;

    /**
     * 发货时间
     */
    private LocalDateTime shipped_at;

    /**
     * 完成时间
     */
    private LocalDateTime completed_at;

    /**
     * 取消时间
     */
    private LocalDateTime cancelled_at;

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
        Orders other = (Orders) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOrder_no() == null ? other.getOrder_no() == null : this.getOrder_no().equals(other.getOrder_no()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getReceiver_name() == null ? other.getReceiver_name() == null : this.getReceiver_name().equals(other.getReceiver_name()))
            && (this.getReceiver_phone() == null ? other.getReceiver_phone() == null : this.getReceiver_phone().equals(other.getReceiver_phone()))
            && (this.getReceiver_address() == null ? other.getReceiver_address() == null : this.getReceiver_address().equals(other.getReceiver_address()))
            && (this.getTotal_amount() == null ? other.getTotal_amount() == null : this.getTotal_amount().equals(other.getTotal_amount()))
            && (this.getFreight_amount() == null ? other.getFreight_amount() == null : this.getFreight_amount().equals(other.getFreight_amount()))
            && (this.getPayable_amount() == null ? other.getPayable_amount() == null : this.getPayable_amount().equals(other.getPayable_amount()))
            && (this.getOrder_status() == null ? other.getOrder_status() == null : this.getOrder_status().equals(other.getOrder_status()))
            && (this.getPayment_status() == null ? other.getPayment_status() == null : this.getPayment_status().equals(other.getPayment_status()))
            && (this.getDelivery_status() == null ? other.getDelivery_status() == null : this.getDelivery_status().equals(other.getDelivery_status()))
            && (this.getUser_remark() == null ? other.getUser_remark() == null : this.getUser_remark().equals(other.getUser_remark()))
            && (this.getCancel_reason() == null ? other.getCancel_reason() == null : this.getCancel_reason().equals(other.getCancel_reason()))
            && (this.getPaid_at() == null ? other.getPaid_at() == null : this.getPaid_at().equals(other.getPaid_at()))
            && (this.getShipped_at() == null ? other.getShipped_at() == null : this.getShipped_at().equals(other.getShipped_at()))
            && (this.getCompleted_at() == null ? other.getCompleted_at() == null : this.getCompleted_at().equals(other.getCompleted_at()))
            && (this.getCancelled_at() == null ? other.getCancelled_at() == null : this.getCancelled_at().equals(other.getCancelled_at()))
            && (this.getCreated_at() == null ? other.getCreated_at() == null : this.getCreated_at().equals(other.getCreated_at()))
            && (this.getUpdated_at() == null ? other.getUpdated_at() == null : this.getUpdated_at().equals(other.getUpdated_at()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOrder_no() == null) ? 0 : getOrder_no().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getReceiver_name() == null) ? 0 : getReceiver_name().hashCode());
        result = prime * result + ((getReceiver_phone() == null) ? 0 : getReceiver_phone().hashCode());
        result = prime * result + ((getReceiver_address() == null) ? 0 : getReceiver_address().hashCode());
        result = prime * result + ((getTotal_amount() == null) ? 0 : getTotal_amount().hashCode());
        result = prime * result + ((getFreight_amount() == null) ? 0 : getFreight_amount().hashCode());
        result = prime * result + ((getPayable_amount() == null) ? 0 : getPayable_amount().hashCode());
        result = prime * result + ((getOrder_status() == null) ? 0 : getOrder_status().hashCode());
        result = prime * result + ((getPayment_status() == null) ? 0 : getPayment_status().hashCode());
        result = prime * result + ((getDelivery_status() == null) ? 0 : getDelivery_status().hashCode());
        result = prime * result + ((getUser_remark() == null) ? 0 : getUser_remark().hashCode());
        result = prime * result + ((getCancel_reason() == null) ? 0 : getCancel_reason().hashCode());
        result = prime * result + ((getPaid_at() == null) ? 0 : getPaid_at().hashCode());
        result = prime * result + ((getShipped_at() == null) ? 0 : getShipped_at().hashCode());
        result = prime * result + ((getCompleted_at() == null) ? 0 : getCompleted_at().hashCode());
        result = prime * result + ((getCancelled_at() == null) ? 0 : getCancelled_at().hashCode());
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
        sb.append(", order_no=").append(order_no);
        sb.append(", user_id=").append(user_id);
        sb.append(", receiver_name=").append(receiver_name);
        sb.append(", receiver_phone=").append(receiver_phone);
        sb.append(", receiver_address=").append(receiver_address);
        sb.append(", total_amount=").append(total_amount);
        sb.append(", freight_amount=").append(freight_amount);
        sb.append(", payable_amount=").append(payable_amount);
        sb.append(", order_status=").append(order_status);
        sb.append(", payment_status=").append(payment_status);
        sb.append(", delivery_status=").append(delivery_status);
        sb.append(", user_remark=").append(user_remark);
        sb.append(", cancel_reason=").append(cancel_reason);
        sb.append(", paid_at=").append(paid_at);
        sb.append(", shipped_at=").append(shipped_at);
        sb.append(", completed_at=").append(completed_at);
        sb.append(", cancelled_at=").append(cancelled_at);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append("]");
        return sb.toString();
    }
}