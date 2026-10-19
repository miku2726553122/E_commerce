package com.example.es.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Orders;
import com.example.es.service.OrdersService;
import com.example.es.mapper.OrdersMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

}




