package com.example.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.es.entity.ProductSkus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86159
* @description 针对表【product_skus(商品SKU表)】的数据库操作Mapper
* @createDate 2026-10-19 10:21:26
* @Entity com.example.es.entity.ProductSkus
*/
public interface ProductSkusMapper extends BaseMapper<ProductSkus> {

    /**
     * 根据商品ID查询SKU列表（启用的）
     */
    default List<ProductSkus> findByProductId(Long productId){
        LambdaQueryWrapper<ProductSkus> wrapper = Wrappers.<ProductSkus>lambdaQuery()
                .eq(ProductSkus::getProductId, productId)
                .eq(ProductSkus::getStatus, 1);
        return selectList(wrapper);
    }
}




