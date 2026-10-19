package com.example.es.enums;

import com.example.es.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ----------- 通用异常状态码 -----------
    SUCCESS("200", "成功"),
    FAIL("500", "失败"),
    PARAM_ERROR("400", "参数错误"),
    NOT_FOUND("404", "资源未找到"),
    INTERNAL_SERVER_ERROR("500", "服务器错误");

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;
}
