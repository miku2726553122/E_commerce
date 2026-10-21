package com.example.es.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAddressReqVO {
    /**
     * 收货人
     */
    private String receiverName;

    /**
     * 电话
     */
    private String receiverPhone;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 是否默认
     */
    private Boolean isDefault;
}
