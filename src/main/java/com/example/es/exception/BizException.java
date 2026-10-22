package com.example.es.exception;

import com.example.es.enums.ResponseCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BizException extends RuntimeException {
    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

    public BizException(String message) {
        super(message);
        this.errorCode = "ORDER_ERROR";
        this.errorMessage = message;
    }

    public BizException(BaseExceptionInterface baseExceptionInterface) {
        super(baseExceptionInterface.getErrorMessage());
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }

    /**
     * 使用枚举创建异常
     */
    public BizException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum.getErrorMessage());
        this.errorCode = responseCodeEnum.getErrorCode();
        this.errorMessage = responseCodeEnum.getErrorMessage();
    }
}
