package com.example.es.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Users;
import com.example.es.service.UsersService;
import com.example.es.mapper.UsersMapper;
import com.example.es.utils.Response;
import com.example.es.vo.admin.FindAllRoleReqVO;
import com.example.es.vo.admin.FindByIdRspVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【users(用户表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Response selectAllRole(FindAllRoleReqVO reqVO) {
        Page<Users> page = usersMapper.selectPageList(reqVO.getPageNum(), reqVO.getPageSize(), reqVO.getRole(), reqVO.getStatus(), reqVO.getKeyword());
        return Response.success(page);
    }

    @Override
    public Response selectById(Long id) {
        Users users = usersMapper.selectById(id);
        FindByIdRspVO findByIdRspVO = FindByIdRspVO.builder()
                .id(users.getId())
                .username(users.getUsername())
                .nickname(users.getNickname())
                .role(users.getRole())
                .status(users.getStatus())
                .build();
        return Response.success(findByIdRspVO);
    }
}




