package com.example.es.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Users;
import com.example.es.service.UsersService;
import com.example.es.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【users(用户表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




