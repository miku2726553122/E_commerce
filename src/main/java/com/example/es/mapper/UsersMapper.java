package com.example.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.es.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.es.utils.PasswordUtil;
import com.example.es.vo.user.UpdatePasswordReqVO;
import com.example.es.vo.user.UpdateUserInfoReqVO;

import java.util.Objects;

/**
 * @author 86159
 * @description 针对表【users(用户表)】的数据库操作Mapper
 * @createDate 2026-10-19 10:21:26
 * @Entity com.example.es.entity.Users
 */
public interface UsersMapper extends BaseMapper<Users> {
    default Page<Users> selectPageList(Integer pageNum, Integer pageSize, String role,
                                        Integer status, String keyword) {
        // 分页对象(查询第几页、每页多少数据)
        Page<Users> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<Users> wrapper = Wrappers.<Users>lambdaQuery()
                .eq(StringUtils.isNotBlank(role), Users::getRole, role)           // 精确匹配角色
                .eq(Objects.nonNull(status), Users::getStatus, status)             // 精确匹配状态
                .like(StringUtils.isNotBlank(keyword), Users::getRole, keyword)    // 关键词模糊查询
                .orderByDesc(Users::getCreatedAt);                                // 按创建时间倒序

        return selectPage(page, wrapper);
    }

    /**
     * 根据用户名查询用户
     */
    default Users findByUsername(String username) {
        LambdaQueryWrapper<Users> wrapper = Wrappers.<Users>lambdaQuery()
                .eq(Users::getUsername, username);
        return selectOne(wrapper);
    }

    /**
     * 根据用户名更新用户信息
     */
    default boolean updateUserInfo(UpdateUserInfoReqVO reqVO, String username){
        LambdaUpdateWrapper<Users> wrapper = Wrappers.<Users>lambdaUpdate()
                .eq(Users::getUsername, username)
                .set(reqVO.getNickname() != null, Users::getNickname, reqVO.getNickname())
                .set(reqVO.getRealName() != null, Users::getRealName, reqVO.getRealName())
                .set(reqVO.getPhone() != null, Users::getPhone, reqVO.getPhone())
                .set(reqVO.getAvatar() != null, Users::getAvatar, reqVO.getAvatar());
        return update(wrapper) > 0;
    }

    default boolean updatePassword(UpdatePasswordReqVO updatePasswordReqVO, String username){
        LambdaUpdateWrapper<Users> wrapper = Wrappers.<Users>lambdaUpdate()
                .eq(Users::getUsername, username)
                .set(Users::getPasswordHash, PasswordUtil.encode(updatePasswordReqVO.getNewPassword()));
        return update(wrapper) > 0;
    }
}




