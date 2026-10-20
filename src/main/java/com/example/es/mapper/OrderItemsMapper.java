package com.example.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.es.entity.OrderItems;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
* @author 86159
* @description 针对表【order_items(订单明细表)】的数据库操作Mapper
* @createDate 2026-10-19 10:21:26
* @Entity com.example.es.entity.OrderItems
*/
public interface OrderItemsMapper extends BaseMapper<OrderItems> {

    default List<OrderItems> findOrderItemsById(Long id){
        LambdaQueryWrapper<OrderItems> wrapper = Wrappers.<OrderItems>lambdaQuery()
                .eq(OrderItems::getOrderId, id);
        return selectList(wrapper);
    }
}




