package com.example.es.service;

import com.example.es.entity.Orders;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.FindPageByOrdersReqVO;
import com.example.es.vo.admin.UpdateOrderStatusReqVO;
import com.example.es.vo.user.CancelOrderReqVO;
import com.example.es.vo.user.CreateOrderReqVO;
import com.example.es.vo.user.UserOrderPageReqVO;

/**
* @author 86159
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface OrdersService extends IService<Orders> {

    Response findPageByOrders(FindPageByOrdersReqVO findPageByOrdersReqVO);

    Response findOrdersById(Long id);

    Response updateOrderStatus(Long id, UpdateOrderStatusReqVO updateOrderStatusReqVO);

    /**
     * 用户提交订单
     *
     * @param reqVO 订单信息
     * @return 订单信息（id, orderNo, payableAmount, orderStatus）
     */
    Response createOrder(CreateOrderReqVO reqVO);

    /**
     * 用户查询我的订单（分页）
     *
     * @param reqVO 查询条件
     * @return 分页订单列表
     */
    Response getUserOrders(UserOrderPageReqVO reqVO);

    /**
     * 用户查询订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    Response getUserOrderById(Long id);

    /**
     * 用户取消订单
     *
     * @param id    订单ID
     * @param reqVO 取消原因
     * @return 取消是否成功
     */
    Response cancelOrder(Long id, CancelOrderReqVO reqVO);

    /**
     * 用户确认收货
     *
     * @param id 订单ID
     * @return 确认是否成功
     */
    Response receiveOrder(Long id);
}
