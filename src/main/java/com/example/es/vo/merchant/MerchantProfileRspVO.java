package com.example.es.vo.merchant;//查询商家店铺资料接口返回给前端的数据。

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "查询商家店铺")
public class MerchantProfileRspVO {
    private Long id;
    private String username;
    private String shopName;
    private String shopLogo;
}