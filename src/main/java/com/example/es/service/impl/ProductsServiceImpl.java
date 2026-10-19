package com.example.es.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Products;
import com.example.es.service.ProductsService;
import com.example.es.mapper.ProductsMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【products(商品表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products>
    implements ProductsService{

}




