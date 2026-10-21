package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.UserAddresses;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.mapper.UserAddressesMapper;
import com.example.es.mapper.UsersMapper;
import com.example.es.service.UserAddressesService;
import com.example.es.utils.Response;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.user.AddressRspVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* @author 86159
* @description 针对表【user_addresses(用户收货地址表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class UserAddressesServiceImpl extends ServiceImpl<UserAddressesMapper, UserAddresses>
    implements UserAddressesService{

    @Autowired
    private UserAddressesMapper userAddressesMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Response getUserAddresses() {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 根据用户名查询用户，获取 userId
        Long userId = usersMapper.findByUsername(username).getId();

        // 3. 根据 userId 查询收货地址列表
        LambdaQueryWrapper<UserAddresses> wrapper = Wrappers.<UserAddresses>lambdaQuery()
                .eq(UserAddresses::getUserId, userId)
                .orderByDesc(UserAddresses::getIsDefault)
                .orderByDesc(UserAddresses::getCreatedAt);
        List<UserAddresses> addresses = userAddressesMapper.selectList(wrapper);

        if (addresses == null || addresses.isEmpty()) {
            return Response.success(Collections.emptyList());
        }

        // 4. 转换为 VO 列表
        List<AddressRspVO> voList = new ArrayList<>();
        for (UserAddresses addr : addresses) {
            AddressRspVO vo = AddressRspVO.builder()
                    .id(addr.getId())
                    .receiverName(addr.getReceiverName())
                    .receiverPhone(addr.getReceiverPhone())
                    .detailAddress(addr.getDetailAddress())
                    .isDefault(addr.getIsDefault() != null && addr.getIsDefault() == 1)
                    .build();
            voList.add(vo);
        }

        return Response.success(voList);
    }
}




