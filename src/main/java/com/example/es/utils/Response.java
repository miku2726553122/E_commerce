package com.example.es.utils;

import com.example.es.enums.ResponseCodeEnum;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonPropertyOrder({"success", "message", "data"})
public class Response<T> implements Serializable {
    // 是否成功
    private boolean success;
    // 提示信息
    private String message;
    // 返回数据
    private T data;

    // =================================== 成功响应 ===================================
    public static <T> Response<T> success() {
        Response<T> response = new Response<>();
        response.setMessage("成功");
        response.setSuccess(true);
        return response;
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setMessage("成功");
        response.setSuccess(true);
        return response;
    }
    // =================================== 失败响应 ===================================
    public static <T> Response<T> fail() {
        Response<T> response = new Response<>();
        response.setMessage("失败");
        response.setSuccess(false);
        return response;
    }
    public static <T> Response<T> fail(String message) {
        Response<T> response = new Response<>();
        response.setMessage(message);
        response.setSuccess(false);
        return response;
    }

    public static <T> Response<T> fail(ResponseCodeEnum responseCode) {
        Response<T> response = new Response<>();
        response.setMessage(responseCode.getErrorMessage());
        response.setSuccess(false);
        return response;
    }
}
