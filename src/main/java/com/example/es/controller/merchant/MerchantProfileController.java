package com.example.es.controller.merchant;

import com.example.es.service.UsersService;
import com.example.es.utils.Response;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.merchant.UpdateMerchantProfileReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "商家-店铺资料", description = "查询与修改当前登录商家的店铺资料")
@RestController
@RequestMapping("/merchant")
public class MerchantProfileController {

    private final UsersService usersService;

    public MerchantProfileController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(summary = "查询店铺信息", description = "GET /merchant/profile")
    @GetMapping("/profile")
    public Response getProfile() {
        return usersService.getMerchantProfile(SecurityUtils.getCurrentUserId());
    }

    @Operation(summary = "修改店铺信息", description = "PUT /merchant/profile，三个字段均可选")
    @PutMapping("/profile")
    public Response updateProfile(@RequestBody UpdateMerchantProfileReqVO reqVO) {
        return usersService.updateMerchantProfile(SecurityUtils.getCurrentUserId(), reqVO);
    }
}