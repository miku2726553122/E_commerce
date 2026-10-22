package com.example.es.controller.auth;

import com.example.es.service.CaptchaService;
import com.example.es.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 图形验证码接口
 *
 * @author 86159
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "CaptchaController", description = "图形验证码相关操作")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping("/captcha")
    @Operation(summary = "获取图形验证码", description = "返回 captchaKey 和 Base64 编码的验证码图片")
    public Response<Map<String, String>> getCaptcha() {
        Map<String, String> data = captchaService.generateCaptcha();
        return Response.success(data);
    }
}
