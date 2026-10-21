package com.example.es.controller.admin;

import com.example.es.service.UsersService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.AddMerchantsReqVO;
import com.example.es.vo.admin.FindAllRoleReqVO;
import com.example.es.vo.admin.ResetPasswordReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员接口", description = "管理员相关接口")
@RestController
@RequestMapping("/admin")
public class UsersAndMerchantsController {

    @Autowired
    private UsersService usersService;

    @Operation(summary = "测试接口", description = "这是一个测试接口")
    @GetMapping("/test")
    public String test() {
        return "Hello Admin";
    }

    /**
     * 查询所有用户（支持分页和筛选）
     *
     * @param reqVO
     * @return
     */
    @Operation(summary = "查询所有用户", description = "查询所有用户，支持分页和筛选")
    @PostMapping("/users")
    public Response selectAllRole(@RequestBody FindAllRoleReqVO reqVO) {
       return  usersService.selectAllRole(reqVO);
    }

    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    @Operation(summary = "根据id获取用户信息", description = "根据id获取用户信息")
    public Response selectById(@PathVariable Long id) {
        return usersService.selectById(id);
    }


    /**
     * 添加商户
     * @param addMerchantsReqVO
     * @return
     */
    @PostMapping("/merchants")
    @Operation(summary = "添加商户", description = "添加商户")
    public Response addMerchants(@RequestBody @Validated AddMerchantsReqVO addMerchantsReqVO) {
        return usersService.addMerchant(addMerchantsReqVO);
    }

    /**
     * 启用或禁用用户
     * @param id
     * @param status
     * @return
     */

    @PutMapping("/users/{id}/status")
    @Operation(summary = "启用或禁用用户", description = "启用或禁用用户")
    public Response EDUser(@PathVariable Long id, @RequestParam Integer status) {
        return usersService.enableOrDisableUser(id, status);
    }

    /**
     * 重置用户密码
     * @param id
      * @param reqVO
     * @return
     */

    @PutMapping("/users/{id}/reset-Password")
    @Operation(summary = "重置用户密码", description = "重置用户密码")
    public Response resetPassword(@PathVariable Long id, @RequestBody @Validated ResetPasswordReqVO reqVO) {
        return usersService.resetPassword(id, reqVO.getNewPassword());
    }


    @Operation(summary = "删除用户信息", description = "删除用户信息")
    @DeleteMapping("/users/{id}")
    public Response deleteUserInfo(@PathVariable Long id){
        return usersService.deleteUserInfo(id);
    }

}
