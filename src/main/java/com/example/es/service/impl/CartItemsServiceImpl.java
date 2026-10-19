package com.example.es.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.CartItems;
import com.example.es.service.CartItemsService;
import com.example.es.mapper.CartItemsMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【cart_items(购物车明细表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class CartItemsServiceImpl extends ServiceImpl<CartItemsMapper, CartItems>
    implements CartItemsService{

}




