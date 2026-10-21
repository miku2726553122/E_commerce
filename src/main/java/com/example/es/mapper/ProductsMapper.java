package com.example.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.es.entity.Products;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.es.vo.admin.FindAllProductsReqVO;
import com.example.es.vo.user.FindUserProductsReqVO;

import java.util.Objects;

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

    default boolean updateProductShelf(Long id, Integer saleStatus){
        return updateById(Products.builder()
                .id(id)
                .saleStatus(saleStatus)
                .build()) > 0;
    }

    /**
     * 用户端分页查询商品（已上架且审核通过）
     */
    default Page<Products> findUserProducts(FindUserProductsReqVO reqVO){
        Page<Products> page = new Page<>(reqVO.getPageNum(), reqVO.getPageSize());

        LambdaQueryWrapper<Products> wrapper = Wrappers.<Products>lambdaQuery()
                .eq(reqVO.getCategoryId() != null, Products::getCategoryId, reqVO.getCategoryId())
                .like(StringUtils.isNotBlank(reqVO.getKeyword()), Products::getProductName, reqVO.getKeyword())
                .eq(Products::getSaleStatus, 1)
                .eq(Products::getAuditStatus, 1)
                .orderByDesc(Products::getCreatedAt);

        return selectPage(page, wrapper);
    }

    /**
     * 根据ID查询商品详情（已上架且审核通过）
     */
    default Products findProductById(Long id){
        LambdaQueryWrapper<Products> wrapper = Wrappers.<Products>lambdaQuery()
                .eq(Products::getId, id)
                .eq(Products::getSaleStatus, 1)
                .eq(Products::getAuditStatus, 1);
        return selectOne(wrapper);
    }
}




