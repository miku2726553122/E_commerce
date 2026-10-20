package com.example.es.auth;

import com.example.es.enums.ResponseCodeEnum;
import com.example.es.exception.BizException;
import com.example.es.utils.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    @Value("${app.merchant.dev-merchant-id:0}")
    private Long devMerchantId;

    public Long getMerchantId() {
        if (devMerchantId==null || devMerchantId <= 0) {
            throw new BizException(ResponseCodeEnum.UNAUTHORIZED);
        }
        return devMerchantId;
    }
}
