package com.example.es.service;

import com.example.es.entity.Orders;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.FindPageByOrdersReqVO;
import com.example.es.vo.admin.UpdateOrderStatusReqVO;

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
     * 查询商家关联订单列表
     */
    Response getMerchantOrders(Long merchantId);

    /**
     * 查询商家订单详情
     */
    Response getMerchantOrderDetail(Long merchantId, Long orderId);

    /**
     * 商家确认发货
     */
    Response confirmShipment(Long merchantId, Long orderId);

    /**
     * 用户创建订单
     */
    Response createUserOrder(Long userId, com.example.es.vo.user.order.CreateOrderReqVO reqVO);

    /**
     * 用户查询订单列表
     */
    Response getUserOrders(Long userId, int pageNum, int pageSize);

    Response simulatePayment(Long userId, Long orderId);

    /**
     * 用户查询订单详情
     */
    Response getUserOrderDetail(Long userId, Long orderId);

    /**
     * 用户取消订单
     */
    Response cancelUserOrder(Long userId, Long orderId, com.example.es.vo.user.order.CancelOrderReqVO reqVO);

    /**
     * 用户确认收货
     */
    Response confirmReceipt(Long userId, Long orderId);

}
