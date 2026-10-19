package com.example.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.es.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Objects;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;
import static javax.management.Query.eq;

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

}




