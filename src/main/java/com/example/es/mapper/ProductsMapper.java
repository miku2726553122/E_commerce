package com.example.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.es.entity.Products;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.es.vo.admin.FindAllProductsReqVO;

/**
* @author 86159
* @description 针对表【products(商品表)】的数据库操作Mapper
* @createDate 2026-10-19 10:21:26
* @Entity com.example.es.entity.Products
*/
public interface ProductsMapper extends BaseMapper<Products> {

    default Page<Products> findAllProducts(FindAllProductsReqVO findAllProductsReqVO){
        Page<Products> page = new Page<>(findAllProductsReqVO.getPageNum(), findAllProductsReqVO.getPageSize());

        //构建查询
        LambdaQueryWrapper<Products> wrapper = Wrappers.<Products>lambdaQuery()
                .eq(Products::getMerchantId, findAllProductsReqVO.getMerchantId())
                .eq(Products::getAuditStatus, findAllProductsReqVO.getAuditStatus())
                .eq(Products::getSaleStatus, findAllProductsReqVO.getSaleStatus())
                .orderByDesc(Products::getCreatedAt);

        return selectPage(page, wrapper);
    }
}




