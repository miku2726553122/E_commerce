package com.example.es.service;

import com.example.es.entity.UserAddresses;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;

/**
* @author 86159
* @description 针对表【user_addresses(用户收货地址表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface UserAddressesService extends IService<UserAddresses> {

    /**
     * 查询当前登录用户的所有收货地址
     *
     * @return 收货地址列表
     */
    Response getUserAddresses();
}
