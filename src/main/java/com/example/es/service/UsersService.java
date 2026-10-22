package com.example.es.service;

import com.example.es.entity.Users;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.auth.LoginReqVO;
import com.example.es.vo.auth.UserRegisterReqVO;
import com.example.es.vo.auth.MerchantRegisterReqVO;
import com.example.es.vo.admin.AddMerchantsReqVO;
import com.example.es.vo.admin.FindAllRoleReqVO;
import com.example.es.vo.merchant.UpdateMerchantProfileReqVO;
import com.example.es.vo.user.UpdatePasswordReqVO;
import com.example.es.vo.user.UpdateUserInfoReqVO;

/**
* @author 86159
* @description 针对表【users(用户表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface UsersService extends IService<Users> {
    Response selectAllRole(FindAllRoleReqVO reqVO);

    Response selectById(Long id);

    Response addMerchant(AddMerchantsReqVO addMerchantsReqVO);

    Response enableOrDisableUser(Long id, Integer status);

    Response resetPassword(Long id, String newPassword);

    Response getMerchantProfile(Long merchantId);

    Response updateMerchantProfile(Long merchantId, UpdateMerchantProfileReqVO reqVO);

    Response deleteUserInfo(Long id);

    Response login(LoginReqVO loginReqVO);

    /**
     * 用户注册（默认角色 USER）
     */
    Response userRegister(UserRegisterReqVO reqVO);

    /**
     * 商家注册（默认角色 MERCHANT）
     */
    Response merchantRegister(MerchantRegisterReqVO reqVO);

    Response FindUserInfo();

    Response UpdateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO);

    Response UpdatePassword(UpdatePasswordReqVO updatePasswordReqVO);
}
