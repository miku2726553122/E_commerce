package com.example.es.service.impl;

import com.example.es.entity.Users;
import com.example.es.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Spring Security UserDetailsService 实现：从 users 表加载用户及其角色。
 *
 * @author 86159
 */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersMapper.findByUsername(username);
        if (Objects.isNull(users)) {
            throw new UsernameNotFoundException("该用户不存在");
        }

        // users.role 是单角色字段，直接拼 ROLE_ 前缀作为 authority
        String role = users.getRole();
        String[] roleArr = (role == null || role.isBlank())
                ? new String[0]
                : new String[]{role.startsWith("ROLE_") ? role : "ROLE_" + role};

        return User.withUsername(users.getUsername())
                .password(users.getPasswordHash())
                .authorities(roleArr)
                // status == 1 才视为启用
                .disabled(users.getStatus() == null || users.getStatus() != 1)
                .build();
    }
}
