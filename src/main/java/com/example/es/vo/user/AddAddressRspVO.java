package com.example.es.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddAddressRspVO {
    /**
     * 地址ID
     */
    private Long id;

    /**
     * 收货人
     */
    private String receiverName;

    /**
     * 是否默认
     */
    private Boolean isDefault;
}
