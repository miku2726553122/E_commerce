package com.example.es.controller.user;

import com.example.es.service.UsersService;
import com.example.es.utils.Response;
import com.example.es.vo.user.UpdatePasswordReqVO;
import com.example.es.vo.user.UpdateUserInfoReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "普通用户接口", description = "用户信息相关操作")
public class UserInfoController {

    @Autowired
    private UsersService usersService;

    /**
     * 获取当前用户信息
     */

    @GetMapping("/current")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的信息")
    public Response FindUserInfo() {
        return usersService.FindUserInfo();
    }

    /**
     * 更新用户信息
     */

    @PutMapping("/profile")
    @Operation(summary = "更新用户信息", description = "更新当前登录用户的信息")
    public Response UpdateUserInfo(@RequestBody UpdateUserInfoReqVO updateUserInfoReqVO) {
        return usersService.UpdateUserInfo(updateUserInfoReqVO);
    }

    /**
     * 更新用户密码
     */
    @PutMapping("/password")
    @Operation(summary = "更新用户密码", description = "更新当前登录用户密码")
    public Response UpdatePassword(@RequestBody @Validated UpdatePasswordReqVO updatePasswordReqVO) {
        return usersService.UpdatePassword(updatePasswordReqVO);
    }

}
