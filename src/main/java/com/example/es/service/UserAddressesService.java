package com.example.es.service;

import com.example.es.entity.UserAddresses;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.user.AddAddressReqVO;
import com.example.es.vo.user.UpdateAddressReqVO;

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

    /**
     * 新增收货地址
     *
     * @param reqVO 收货地址信息
     * @return 新增的地址信息（id, receiverName, isDefault）
     */
    Response addAddress(AddAddressReqVO reqVO);

    /**
     * 修改收货地址
     *
     * @param id    地址ID
     * @param reqVO 修改信息
     * @return 修改是否成功
     */
    Response updateAddress(Long id, UpdateAddressReqVO reqVO);

    /**
     * 删除收货地址
     *
     * @param id 地址ID
     * @return 删除是否成功
     */
    Response deleteAddress(Long id);

    /**
     * 设置默认地址
     *
     * @param id 地址ID
     * @return 设置是否成功
     */
    Response setDefaultAddress(Long id);
}
