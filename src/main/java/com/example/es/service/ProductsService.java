package com.example.es.service;

import com.example.es.entity.Products;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.shopproduct.CreateMerchantProductReqVO;
import com.example.es.utils.Response;
import com.example.es.vo.admin.FindAllProductsReqVO;
import com.example.es.vo.admin.FindAndUpdateReviewProductReqVO;
import com.example.es.vo.user.FindUserProductsReqVO;
import com.example.es.vo.user.ProductDetailRspVO;

/**
* @author 86159
* @description 针对表【products(商品表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface ProductsService extends IService<Products> {
    Response createMerchantProduct(Long merchantId,
                                   CreateMerchantProductReqVO reqVO);

    Response findAllProducts(FindAllProductsReqVO findAllProductsReqVO);

    Response reviewProduct(Long id, FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO);

    Response updateProductShelf(Long id, FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO);

    Response findUserProducts(FindUserProductsReqVO reqVO);

    Response findProductDetail(Long id);

}
