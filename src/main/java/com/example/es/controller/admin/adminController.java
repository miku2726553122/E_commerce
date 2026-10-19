package com.example.es.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员接口", description = "管理员相关接口")
@RestController
@RequestMapping("/admin")
public class adminController {

    @Operation(summary = "测试接口", description = "这是一个测试接口")
    @GetMapping("/test")
    public String test() {
        return "Hello Admin";
    }
}
