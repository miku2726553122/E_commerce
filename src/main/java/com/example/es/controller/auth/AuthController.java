package com.example.es.controller.auth;

import com.example.es.service.UsersService;
import com.example.es.utils.Response;
import com.example.es.vo.auth.LoginReqVO;
import com.example.es.vo.auth.UserRegisterReqVO;
import com.example.es.vo.auth.MerchantRegisterReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthController", description = "Authentication related operations")
@Slf4j
public class AuthController {

    @Autowired
    private UsersService usersService;


    @PostMapping("/login")
    @Operation(summary = "Login", description = "User login endpoint")
    public Response login(@RequestBody LoginReqVO loginReqVO) {
        log.info("[登录] username={}", loginReqVO.getUsername());
        Response response = usersService.login(loginReqVO);
        log.info("[登录] 结果: success={}", response.isSuccess());
        return response;
    }

    @PostMapping("/register/user")
    @Operation(summary = "用户注册", description = "普通用户注册，默认角色 USER")
    public Response userRegister(@RequestBody @Valid UserRegisterReqVO reqVO) {
        log.info("[用户注册] username={}", reqVO.getUsername());
        Response response = usersService.userRegister(reqVO);
        log.info("[用户注册] 结果: success={}", response.isSuccess());
        return response;
    }

    @PostMapping("/register/merchant")
    @Operation(summary = "商家注册", description = "商家注册，默认角色 MERCHANT")
    public Response merchantRegister(@RequestBody @Valid MerchantRegisterReqVO reqVO) {
        log.info("[商家注册] username={}, shopName={}", reqVO.getUsername(), reqVO.getShopName());
        Response response = usersService.merchantRegister(reqVO);
        log.info("[商家注册] 结果: success={}", response.isSuccess());
        return response;
    }
}

