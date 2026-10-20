package com.example.es.service;

import com.example.es.entity.Users;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.UserDetail;
import com.example.es.vo.admin.AddMechantsReqVO;
import com.example.es.vo.admin.FindAllRoleReqVO;
import org.springframework.http.ResponseEntity;

/**
* @author 86159
* @description 针对表【users(用户表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface UsersService extends IService<Users> {
    Response selectAllRole(FindAllRoleReqVO reqVO);

    Response selectById(Long id);

    Response addMerchant(AddMechantsReqVO addMechantsReqVO);

    Response enableOrDisableUser(Long id, Integer status);

    Response resetPassword(Long id, String newPassword);

    Response deleteUserInfo(Long id);

    ResponseEntity<String> login(UserDetail userDetail);
}
