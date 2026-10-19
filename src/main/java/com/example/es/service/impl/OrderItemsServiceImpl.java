package com.example.es.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.OrderItems;
import com.example.es.service.OrderItemsService;
import com.example.es.mapper.OrderItemsMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【order_items(订单明细表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class OrderItemsServiceImpl extends ServiceImpl<OrderItemsMapper, OrderItems>
    implements OrderItemsService{

}




