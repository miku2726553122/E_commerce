package com.example.es.service;

import com.example.es.entity.Products;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.CreateMerchantProductReqVO;
import com.example.es.vo.admin.FindAllProductsReqVO;
import com.example.es.vo.admin.FindAndUpdateReviewProductReqVO;
import com.example.es.vo.merchant.PageMerchantProductsReqVO;
import com.example.es.vo.merchant.UpdateMerchantProductStatusReqVO;
import com.example.es.vo.merchant.UpdateMerchantProductsReqVO;
import com.example.es.vo.user.FindUserProductsReqVO;

/**
* @author 86159
* @description 针对表【products(商品表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface ProductsService extends IService<Products> {
    Response createMerchantProduct(Long merchantId,
                                   CreateMerchantProductReqVO reqVO);

    Response pageMerchantProducts(Long merchantId,
                                  PageMerchantProductsReqVO reqVO);

    Response getMerchantProduct(Long merchantId, Long id);

    Response updateMerchantProduct(Long merchantId,
                                   Long id,
                                   UpdateMerchantProductsReqVO reqVO);

    Response deleteMerchantProduct(Long merchantId, Long id);

    Response updateMerchantProductStatus(Long merchantId,
                                         Long id,
                                         UpdateMerchantProductStatusReqVO reqVO);

    Response findAllProducts(FindAllProductsReqVO findAllProductsReqVO);

    Response reviewProduct(Long id, FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO);

    Response updateProductShelf(Long id, FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO);

    Response findUserProducts(FindUserProductsReqVO reqVO);

    Response findProductDetail(Long id);

}
