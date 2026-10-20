package com.example.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.es.entity.Products;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.es.vo.admin.FindAllProductsReqVO;
import org.springframework.util.StringUtils;

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
                .eq(findAllProductsReqVO.getMerchantId() != null, Products::getMerchantId, findAllProductsReqVO.getMerchantId())
                .eq(findAllProductsReqVO.getAuditStatus() != null, Products::getAuditStatus, findAllProductsReqVO.getAuditStatus())
                .eq(findAllProductsReqVO.getSaleStatus() != null, Products::getSaleStatus, findAllProductsReqVO.getSaleStatus())
                .orderByDesc(Products::getCreatedAt);

        return selectPage(page, wrapper);
    }

    default boolean existsById(Long id){
        return selectById(id) != null;
    }

    default boolean reviewProduct(Long id, Integer auditStatus, String auditRemark){
        return updateById(Products.builder()
                .id(id)
                .auditStatus(auditStatus)
                .auditRemark(auditRemark)
                .build()) > 0;
    }

    default boolean updareProductShelf(Long id, Integer saleStatus){
        return updateById(Products.builder()
                .id(id)
                .saleStatus(saleStatus)
                .build()) > 0;
    }
}




