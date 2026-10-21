package com.example.es.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.entity.Carts;
import com.example.es.utils.Response;
import com.example.es.vo.user.AddCartItemReqVO;
import com.example.es.vo.user.UpdateCartItemReqVO;

/**
* @author 86159
* @description 针对表【carts(购物车表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface CartsService extends IService<Carts> {

    /**
     * 查询当前用户的购物车
     *
     * @return 购物车信息（含明细和选中商品总金额）
     */
    Response getCart();

    /**
     * 加入购物车
     *
     * @param reqVO SKU ID 和数量
     * @return 购物车明细（id, skuId, quantity）
     */
    Response addToCart(AddCartItemReqVO reqVO);

    /**
     * 修改购物车明细
     *
     * @param id    购物车明细ID
     * @param reqVO 数量、是否选中（均为可选）
     * @return 修改是否成功
     */
    Response updateCartItem(Long id, UpdateCartItemReqVO reqVO);

    /**
     * 删除购物车明细
     *
     * @param id 购物车明细ID
     * @return 删除是否成功
     */
    Response deleteCartItem(Long id);
}
