package com.example.es.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.es.entity.Carts;
import com.example.es.service.CartsService;
import com.example.es.mapper.CartsMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【carts(购物车表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class CartsServiceImpl extends ServiceImpl<CartsMapper, Carts>
    implements CartsService{

}




