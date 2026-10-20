package com.example.es.exception;

import com.example.es.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器
 *
 * @author 86159
 * @createDate 2026-10-20
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BizException.class)
    public Response handleBizException(BizException e) {
        log.error("业务异常: [{}] {}", e.getErrorCode(), e.getErrorMessage());
        return Response.fail(e.getErrorMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("参数校验失败: {}", message);
        return Response.fail(message);
    }

    /**
     * 处理 IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Response handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法参数: {}", e.getMessage());
        return Response.fail("参数错误: " + e.getMessage());
    }

    /**
     * 静态资源未找到（例如 favicon.ico）：不打堆栈，直接返回 404
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Response handleNoResourceFoundException(NoResourceFoundException e) {
        log.debug("静态资源未找到: {}", e.getResourcePath());
        return Response.fail(com.example.es.enums.ResponseCodeEnum.NOT_FOUND);
    }

    /**
     * 处理其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        log.error("系统异常: ", e);
        return Response.fail("系统繁忙，请稍后再试");
    }
}
