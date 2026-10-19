package com.example.es.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.es.entity.UserAddresses;
import com.example.es.service.UserAddressesService;
import com.example.es.mapper.UserAddressesMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【user_addresses(用户收货地址表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class UserAddressesServiceImpl extends ServiceImpl<UserAddressesMapper, UserAddresses>
    implements UserAddressesService{

}




