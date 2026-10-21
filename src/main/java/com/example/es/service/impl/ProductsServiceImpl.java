package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.ProductSkus;
import com.example.es.entity.Products;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.mapper.ProductSkusMapper;
import com.example.es.mapper.CategoriesMapper;
import com.example.es.service.ProductsService;
import com.example.es.mapper.ProductsMapper;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.shopproduct.*;
import com.example.es.vo.admin.FindAllProductsReqVO;
import com.example.es.vo.admin.FindAllProductsRspVO;
import com.example.es.vo.admin.FindAndUpdateReviewProductReqVO;
import com.example.es.vo.user.FindProductCategoriesRspVO;
import com.example.es.vo.user.FindUserProductsReqVO;
import com.example.es.vo.user.FindUserProductsRspVO;
import com.example.es.vo.user.ProductDetailRspVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.es.vo.merchant.shopproduct.UpdateMerchantProductStatusReqVO;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86159
* @description 针对表【products(商品表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class ProductsServiceImpl extends ServiceImpl<ProductsMapper, Products>
    implements ProductsService{

    @Autowired
    private CategoriesMapper categoriesMapper;

    /**
     * 创建商品
     * @param merchantId
     * @param reqVO
     * @return
     */
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

    @Override
    public Response pageMerchantProducts(Long merchantId,
                                         PageMerchantProductsReqVO reqVO) {
        if (merchantId == null) {
            return Response.fail(ResponseCodeEnum.UNAUTHORIZED);
        }

        if (reqVO == null) {
            reqVO = new PageMerchantProductsReqVO();
        }

        int pageNum = reqVO.getPageNum() == null || reqVO.getPageNum() < 1
                ? 1
                : reqVO.getPageNum();

        int pageSize = reqVO.getPageSize() == null || reqVO.getPageSize() < 1
                ? 10
                : reqVO.getPageSize();

        Page<Products> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Products> wrapper =
                Wrappers.<Products>lambdaQuery()
                        .eq(Products::getMerchantId, merchantId)
                        .eq(reqVO.getSaleStatus() != null,
                                Products::getSaleStatus,
                                reqVO.getSaleStatus())
                        .eq(reqVO.getAuditStatus() != null,
                                Products::getAuditStatus,
                                reqVO.getAuditStatus())
                        .orderByDesc(Products::getCreatedAt);

        productsMapper.selectPage(page, wrapper);

        PageMerchantProductsRspVO rspVO =
                PageMerchantProductsRspVO.builder()
                        .records(page.getRecords())
                        .total(page.getTotal())
                        .pageNum((int) page.getCurrent())
                        .pageSize((int) page.getSize())
                        .build();

        return Response.success(rspVO);
    }

    @Override
    public Response getMerchantProduct(Long merchantId, Long id) {
        if (merchantId == null || id == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Products product = productsMapper.selectById(id);

        if (product == null
                || !merchantId.equals(product.getMerchantId())) {
            return Response.fail("商品不存在");
        }

        List<ProductSkus> skus = productSkusMapper.selectList(
                Wrappers.<ProductSkus>lambdaQuery()
                        .eq(ProductSkus::getProductId, id)
        );

        MerchantProductDetailRspVO rspVO =
                MerchantProductDetailRspVO.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .basePrice(product.getBasePrice())
                        .skus(skus)
                        .build();

        return Response.success(rspVO);
    }

    @Override
    public Response updateMerchantProduct(Long merchantId,
                                          Long id,
                                          UpdateMerchantProductsReqVO reqVO) {
        if (merchantId == null || id == null || reqVO == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        if (reqVO.getProductName() == null
                && reqVO.getDescription() == null
                && reqVO.getDetailContent() == null
                && reqVO.getBasePrice() == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        if (reqVO.getProductName() != null
                && reqVO.getProductName().isBlank()) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        if (reqVO.getBasePrice() != null
                && reqVO.getBasePrice().signum() < 0) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Products product = productsMapper.selectById(id);

        if (product == null
                || !merchantId.equals(product.getMerchantId())) {
            return Response.fail("商品不存在");
        }

        Products update = Products.builder()
                .id(id)
                .productName(reqVO.getProductName())
                .description(reqVO.getDescription())
                .detailContent(reqVO.getDetailContent())
                .basePrice(reqVO.getBasePrice())
                .build();

        boolean updated = this.updateById(update);
        return Response.success(updated);
    }

    @Override
    public Response deleteMerchantProduct(Long merchantId, Long id) {
        if (merchantId == null || id == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Products product = productsMapper.selectById(id);

        if (product == null
                || !merchantId.equals(product.getMerchantId())) {
            return Response.fail("商品不存在");
        }

        boolean deleted = this.removeById(id);
        return Response.success(deleted);
    }

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private ProductSkusMapper productSkusMapper;


    /**
     * 查询商品列表
     * @param findAllProductsReqVO
     * @return
     */
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

    /**
     * 上下架商品
     * @param id
     * @param findAndUpdateReviewProductReqVO
     * @return
     */
    @Override
    public Response updateProductShelf(Long id, FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO) {

        if (!productsMapper.existsById(id)) {
            return Response.fail("商品不存在");
        }

        boolean result = productsMapper.updateProductShelf(id, findAndUpdateReviewProductReqVO.getSaleStatus());
        return result? Response.success():Response.fail("参数错误");
    }

    @Override
    public Response updateMerchantProductStatus(Long merchantId,
                                                Long id,
                                                UpdateMerchantProductStatusReqVO reqVO) {
        // 1. 检查基本参数
        if (merchantId == null
                || id == null
                || reqVO == null
                || reqVO.getSaleStatus() == null
                || (reqVO.getSaleStatus() != 0
                && reqVO.getSaleStatus() != 1)) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 2. 查询商品，并确认商品属于当前商家
        Products product = productsMapper.selectById(id);
        if (product == null
                || !merchantId.equals(product.getMerchantId())) {
            return Response.fail("商品不存在");
        }

        // 3. 只有审核通过的商品才能上架
        if (reqVO.getSaleStatus() == 1
                && !Integer.valueOf(1).equals(product.getAuditStatus())) {
            return Response.fail("商品尚未审核通过，不能上架");
        }

        // 4. 只修改销售状态，不修改商品其他字段
        Products update = Products.builder()
                .id(id)
                .saleStatus(reqVO.getSaleStatus())
                .build();

        boolean updated = this.updateById(update);
        return Response.success(updated);
    }

    /**
     * 用户端分页查询商品
     * @param reqVO
     * @return
     */
    @Override
    public Response findUserProducts(FindUserProductsReqVO reqVO) {
        Page<Products> page = productsMapper.findUserProducts(reqVO);

        FindUserProductsRspVO vos = FindUserProductsRspVO.builder()
                .records(page.getRecords())
                .total(page.getTotal())
                .pageNum((int) page.getCurrent())
                .pageSize((int) page.getSize())
                .build();

        return Response.success(vos);
    }

    /**
     * 查询商品详情
     * @param id
     * @return
     */
    @Override
    public Response findProductDetail(Long id) {
        // 查询商品信息
        Products product = productsMapper.findProductById(id);
        if (product == null) {
            return Response.fail("商品不存在");
        }

        // 查询SKU列表
        List<ProductSkus> skus = productSkusMapper.findByProductId(id);

        // 构建响应
        ProductDetailRspVO vos = ProductDetailRspVO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .mainImage(product.getMainImage())
                .skus(skus)
                .build();

        return Response.success(vos);
    }

}




