package com.example.es.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Products;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.service.ProductsService;
import com.example.es.mapper.ProductsMapper;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.shopproduct.CreateMerchantProductReqVO;
import com.example.es.vo.merchant.shopproduct.CreateMerchantProductRspVO;
import com.example.es.utils.Response;
import com.example.es.vo.admin.FindAllProductsReqVO;
import com.example.es.vo.admin.FindAllProductsRspVO;
import com.example.es.vo.admin.FindAndUpdateReviewProductReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【products(商品表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products>
    implements ProductsService{

    @Override
    public Response createMerchantProduct(Long merchantId,
                                          CreateMerchantProductReqVO reqVO) {
        if (merchantId==null
                ||reqVO==null
                ||reqVO.getCategoryId()==null
                ||reqVO.getProductName()==null
                ||reqVO.getProductName().isBlank()
                ||reqVO.getProductCode()==null
                ||reqVO.getProductCode().isBlank()
                ||reqVO.getBasePrice()==null){
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Products product = Products.builder()
                .merchantId(merchantId)
                .categoryId(reqVO.getCategoryId())
                .productName(reqVO.getProductName())
                .productCode(reqVO.getProductCode())
                .mainImage(reqVO.getMainImage())
                .description(reqVO.getDescription())
                .detailContent(reqVO.getDetailContent())
                .basePrice(reqVO.getBasePrice())
                .auditStatus(0)
                .saleStatus(0)
                .build();

        Boolean save = this.save(product);
        if (!save) {
            return Response.fail(ResponseCodeEnum.FAIL);
        }

        CreateMerchantProductRspVO rspVO = CreateMerchantProductRspVO.builder()
                .id(product.getId())
                .merchantId(product.getMerchantId())
                .auditStatus(product.getAuditStatus())
                .saleStatus(product.getSaleStatus())
                .build();

        return Response.success(rspVO);
    }

    @Autowired
    private ProductsMapper productsMapper;

    @Override
    public Response findAllProducts(FindAllProductsReqVO findAllProductsReqVO) {

        Page<Products> page = productsMapper.findAllProducts(findAllProductsReqVO);

        FindAllProductsRspVO vos = FindAllProductsRspVO.builder()
                .records(page.getRecords())
                .total(page.getTotal())
                .pageNum((int) page.getCurrent())
                .pageSize((int) page.getSize())
                .build();


        return Response.success(vos);
    }

    /**
     * 审核商品
     * @param id
     * @param findAndUpdateReviewProductReqVO
     * @return
     */
    @Override
    public Response reviewProduct(Long id, FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO) {
        //商品是否存在
        if (!productsMapper.existsById(id)) {
            return Response.fail("商品不存在");
        }

        boolean result = productsMapper.reviewProduct(id, findAndUpdateReviewProductReqVO.getAuditStatus(), findAndUpdateReviewProductReqVO.getAuditRemark());

        return result? Response.success():Response.fail("参数错误");
    }

    @Override
    public Response updateProductShelf(Long id, FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO) {

        if (!productsMapper.existsById(id)) {
            return Response.fail("商品不存在");
        }

        boolean result = productsMapper.updateProductShelf(id, findAndUpdateReviewProductReqVO.getSaleStatus());
        return result? Response.success():Response.fail("参数错误");
    }
}




