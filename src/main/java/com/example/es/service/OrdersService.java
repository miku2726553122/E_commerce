package com.example.es.service;

import com.example.es.entity.Orders;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.FindPageByOrdersReqVO;

/**
* @author 86159
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface OrdersService extends IService<Orders> {

    Response findPageByOrders(FindPageByOrdersReqVO findPageByOrdersReqVO);

    Response findOrdersById(Long id);
}
