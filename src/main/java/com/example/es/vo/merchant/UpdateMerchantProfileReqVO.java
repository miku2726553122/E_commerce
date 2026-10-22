package com.example.es.vo.merchant;//更新商家店铺

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "更新商家店铺")
public class UpdateMerchantProfileReqVO {
    private String shopName;
    private String shopLogo;
    private String nickname;
}