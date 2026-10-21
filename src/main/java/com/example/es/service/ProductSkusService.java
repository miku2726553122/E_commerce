package com.example.es.service;

import com.example.es.entity.ProductSkus;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.shopproduct.CreateProductSkuReqVO;
import com.example.es.vo.merchant.shopproduct.UpdateProductSkuReqVO;


public interface ProductSkusService extends IService<ProductSkus> {

    /**
     * 新增商品SKU
     * @param merchantId 当前商家ID（用于权限验证）
     * @param productId 商品ID（来自路径参数）
     * @param reqVO SKU信息
     * @return 新增结果
     */
    Response createProductSku(Long merchantId, Long productId, CreateProductSkuReqVO reqVO);

    /**
     * 修改商品SKU
     */
    Response updateProductSku(Long merchantId, Long productId, Long skuId, UpdateProductSkuReqVO reqVO);

    /**
     * 删除商品SKU
     */
    Response deleteProductSku(Long merchantId, Long productId, Long skuId);

}
