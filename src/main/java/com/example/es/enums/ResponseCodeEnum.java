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
    INTERNAL_SERVER_ERROR("500", "服务器错误"),
    USER_NOT_FOUND("1000", "用户未找到"),
    PASSWORD_SAME("1001", "密码相同"),
    UNAUTHORIZED("401", "未登录或未配置商家身份"),
    FORBIDDEN("403", "当前账号不是商家"),
    USERNAME_PASSWORD_ERROR("1002", "用户名或密码错误"),
    PASSWORD_WEAK("1003", "密码强度不足"),
    ACCOUNT_DISABLED("1004", "账号已禁用"),
    NOT_LOGGED_IN("1005", "未登录"),





    ;

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;
}
