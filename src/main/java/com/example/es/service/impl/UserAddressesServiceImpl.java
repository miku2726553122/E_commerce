package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.UserAddresses;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.mapper.UserAddressesMapper;
import com.example.es.service.UserAddressesService;
import com.example.es.utils.Response;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.user.AddAddressReqVO;
import com.example.es.vo.user.AddAddressRspVO;
import com.example.es.vo.user.AddressRspVO;
import com.example.es.vo.user.UpdateAddressReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 校验登录状态，未登录返回错误响应
     */
    private Response checkLogin() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }
        return null;
    }

    /**
     * 查找地址并校验归属权，失败返回错误响应
     */
    private Response validateAddressOwnership(Long id, Long userId) {
        UserAddresses address = userAddressesMapper.selectById(id);
        if (address == null) {
            return Response.fail("地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            return Response.fail("无权操作该地址");
        }
        return null;
    }

    /**
     * 取消用户其他默认地址
     */
    private void resetOtherDefaultAddresses(Long userId) {
        LambdaQueryWrapper<UserAddresses> resetWrapper = Wrappers.<UserAddresses>lambdaQuery()
                .eq(UserAddresses::getUserId, userId)
                .eq(UserAddresses::getIsDefault, 1);
        List<UserAddresses> existingDefaults = userAddressesMapper.selectList(resetWrapper);
        for (UserAddresses addr : existingDefaults) {
            addr.setIsDefault(0);
            userAddressesMapper.updateById(addr);
        }
    }

    /**
     * 地址实体转 VO
     */
    private AddressRspVO toAddressRspVO(UserAddresses addr) {
        return AddressRspVO.builder()
                .id(addr.getId())
                .receiverName(addr.getReceiverName())
                .receiverPhone(addr.getReceiverPhone())
                .province(addr.getProvince())
                .city(addr.getCity())
                .district(addr.getDistrict())
                .detailAddress(addr.getDetailAddress())
                .isDefault(addr.getIsDefault() != null && addr.getIsDefault() == 1)
                .build();
    }

    @Override
    public Response getUserAddresses() {
        // 1. 获取当前登录用户
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 根据 userId 查询收货地址列表
        LambdaQueryWrapper<UserAddresses> wrapper = Wrappers.<UserAddresses>lambdaQuery()
                .eq(UserAddresses::getUserId, userId)
                .orderByDesc(UserAddresses::getIsDefault)
                .orderByDesc(UserAddresses::getCreatedAt);
        List<UserAddresses> addresses = userAddressesMapper.selectList(wrapper);

        if (addresses == null || addresses.isEmpty()) {
            return Response.success(Collections.emptyList());
        }

        // 3. 转换为 VO 列表
        List<AddressRspVO> voList = addresses.stream()
                .map(this::toAddressRspVO)
                .collect(Collectors.toList());

        return Response.success(voList);
    }

    @Override
    public Response addAddress(AddAddressReqVO reqVO) {
        // 1. 获取当前登录用户
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 参数校验
        if (reqVO.getReceiverName() == null || reqVO.getReceiverPhone() == null
                || reqVO.getProvince() == null || reqVO.getCity() == null
                || reqVO.getDistrict() == null || reqVO.getDetailAddress() == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 3. 如果设置为默认地址，先将该用户其他地址的默认标记取消
        int isDefaultInt = (reqVO.getIsDefault() != null && reqVO.getIsDefault()) ? 1 : 0;
        if (isDefaultInt == 1) {
            resetOtherDefaultAddresses(userId);
        }

        // 4. 构建地址实体并插入
        UserAddresses address = UserAddresses.builder()
                .userId(userId)
                .receiverName(reqVO.getReceiverName())
                .receiverPhone(reqVO.getReceiverPhone())
                .province(reqVO.getProvince())
                .city(reqVO.getCity())
                .district(reqVO.getDistrict())
                .detailAddress(reqVO.getDetailAddress())
                .isDefault(isDefaultInt)
                .build();
        userAddressesMapper.insert(address);

        // 5. 返回结果
        AddAddressRspVO rspVO = AddAddressRspVO.builder()
                .id(address.getId())
                .receiverName(address.getReceiverName())
                .isDefault(isDefaultInt == 1)
                .build();

        return Response.success(rspVO);
    }

    @Override
    public Response updateAddress(Long id, UpdateAddressReqVO reqVO) {
        // 1. 获取当前登录用户
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 校验地址归属权
        Response ownershipError = validateAddressOwnership(id, userId);
        if (ownershipError != null) {
            return ownershipError;
        }

        // 3. 构建更新条件，使用 LambdaUpdateWrapper 支持部分字段更新
        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<UserAddresses> updateWrapper =
                Wrappers.<UserAddresses>lambdaUpdate()
                        .eq(UserAddresses::getId, id);

        boolean needUpdate = false;

        if (reqVO.getReceiverName() != null) {
            updateWrapper.set(UserAddresses::getReceiverName, reqVO.getReceiverName());
            needUpdate = true;
        }
        if (reqVO.getReceiverPhone() != null) {
            updateWrapper.set(UserAddresses::getReceiverPhone, reqVO.getReceiverPhone());
            needUpdate = true;
        }
        if (reqVO.getDetailAddress() != null) {
            updateWrapper.set(UserAddresses::getDetailAddress, reqVO.getDetailAddress());
            needUpdate = true;
        }

        // 处理默认地址逻辑
        if (reqVO.getIsDefault() != null) {
            int isDefaultInt = reqVO.getIsDefault() ? 1 : 0;
            if (isDefaultInt == 1) {
                resetOtherDefaultAddresses(userId);
            }
            updateWrapper.set(UserAddresses::getIsDefault, isDefaultInt);
            needUpdate = true;
        }

        if (!needUpdate) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        boolean result = userAddressesMapper.update(null, updateWrapper) > 0;
        return Response.success(result);
    }

    @Override
    public Response deleteAddress(Long id) {
        // 1. 获取当前登录用户
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 校验地址归属权
        Response ownershipError = validateAddressOwnership(id, userId);
        if (ownershipError != null) {
            return ownershipError;
        }

        // 3. 删除地址
        boolean result = userAddressesMapper.deleteById(id) > 0;
        return Response.success(result);
    }

    @Override
    public Response setDefaultAddress(Long id) {
        // 1. 获取当前登录用户
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 校验地址归属权
        Response ownershipError = validateAddressOwnership(id, userId);
        if (ownershipError != null) {
            return ownershipError;
        }

        // 3. 取消其他默认地址
        resetOtherDefaultAddresses(userId);

        // 4. 设置当前地址为默认
        UserAddresses address = userAddressesMapper.selectById(id);
        address.setIsDefault(1);
        boolean result = userAddressesMapper.updateById(address) > 0;
        return Response.success(result);
    }
}




