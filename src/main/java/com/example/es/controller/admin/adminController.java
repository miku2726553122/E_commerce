package com.example.es.controller.admin;

import com.example.es.service.UsersService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.AddMechantsReqVO;
import com.example.es.vo.admin.FindAllRoleReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员接口", description = "管理员相关接口")
@RestController
@RequestMapping("/admin")
public class adminController {

    @Autowired
    private UsersService usersService;

    @Operation(summary = "测试接口", description = "这是一个测试接口")
    @GetMapping("/test")
    public String test() {
        return "Hello Admin";
    }

    /**
     * 查询所有角色
     *
     * @param reqVO
     * @return
     */
    @Operation(summary = "查询所有角色", description = "查询所有角色")
    @GetMapping("/users")
    public Response selectAllRole(FindAllRoleReqVO reqVO) {
       return  usersService.selectAllRole(reqVO);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "根据id获取用户信息", description = "根据id获取用户信息")
    public Response selectById(Long id) {
        return usersService.selectById(id);
    }


    @GetMapping("/merchants")
    @Operation(summary = "添加商户", description = "添加商户")
    public Response addmerchants(AddMechantsReqVO addMechantsReqVO) {
        return usersService.addMerchant(addMechantsReqVO);
    }
}
