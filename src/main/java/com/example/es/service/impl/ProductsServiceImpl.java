package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Categories;
import com.example.es.entity.ProductSkus;
import com.example.es.entity.Products;
import com.example.es.entity.Users;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.exception.BizException;
import com.example.es.mapper.ProductSkusMapper;
import com.example.es.mapper.CategoriesMapper;
import com.example.es.service.ProductsService;
import com.example.es.mapper.ProductsMapper;
import com.example.es.mapper.UsersMapper;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.*;
import com.example.es.vo.admin.FindAllProductsReqVO;
import com.example.es.vo.admin.FindAllProductsRspVO;
import com.example.es.vo.admin.FindAndUpdateReviewProductReqVO;
import com.example.es.vo.user.FindUserProductsReqVO;
import com.example.es.vo.user.FindUserProductsRspVO;
import com.example.es.vo.user.ProductDetailRspVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 创建商品
     * @param merchantId
     * @param reqVO
     * @return
     */
    @Override
    @Transactional
    public Response createMerchantProduct(Long merchantId,
                                          CreateMerchantProductReqVO reqVO) {
        if (merchantId==null
                ||reqVO==null
                ||reqVO.getProductName()==null
                ||reqVO.getProductName().isBlank()
                ||reqVO.getProductCode()==null
                ||reqVO.getProductCode().isBlank()
                ||reqVO.getBasePrice()==null
                ||reqVO.getBasePrice().signum() <= 0){
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        boolean newCategory = reqVO.getNewCategoryName() != null
                && !reqVO.getNewCategoryName().isBlank();
        if (newCategory == (reqVO.getCategoryId() != null)) {
            return Response.fail("请选择已有分类或申请新分类");
        }
        Categories category;
        if (newCategory) {
            String name = reqVO.getNewCategoryName().trim();
            if (name.length() > 100) {
                return Response.fail("分类名称不能超过100字");
            }
            long parentId = reqVO.getCategoryParentId() == null ? 0L : reqVO.getCategoryParentId();
            if (parentId != 0) {
                Categories parent = categoriesMapper.selectByIdForUpdate(parentId);
                if (parent == null || !Integer.valueOf(1).equals(parent.getStatus())
                        || !Long.valueOf(0).equals(parent.getParentId())) {
                    return Response.fail("请选择启用的一级分类作为父级");
                }
            }
            category = categoriesMapper.selectOne(Wrappers.<Categories>lambdaQuery()
                    .eq(Categories::getParentId, parentId)
                    .eq(Categories::getCategoryName, name)
                    .last("FOR UPDATE"));
            if (category == null) {
                category = Categories.builder().parentId(parentId).categoryName(name)
                        .status(2).sortOrder(0).build();
                if (categoriesMapper.insert(category) != 1) {
                    throw new BizException("分类申请保存失败，请重试");
                }
            } else if (Integer.valueOf(1).equals(category.getStatus())) {
                return Response.fail("该分类已存在，请选择已有分类");
            } else if (!Integer.valueOf(2).equals(category.getStatus())) {
                return Response.fail("同名分类已停用，请联系管理员");
            }
        } else {
            category = categoriesMapper.selectById(reqVO.getCategoryId());
            if (!isCategoryAvailable(category)) {
                return Response.fail("所选分类不存在或未启用");
            }
        }
        Products product = Products.builder()
                .merchantId(merchantId)
                .categoryId(category.getId())
                .productName(reqVO.getProductName().trim())
                .productCode(reqVO.getProductCode().trim())
                .mainImage(reqVO.getMainImage())
                .description(reqVO.getDescription())
                .detailContent(reqVO.getDetailContent())
                .basePrice(reqVO.getBasePrice())
                .auditStatus(0)
                .saleStatus(0)
                .build();

        Boolean save = this.save(product);
        if (!save) {
            throw new BizException("商品提交失败，请重试");
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
        populateCategories(page.getRecords());

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
                && reqVO.getMainImage() == null
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

        // 使用乐观锁重试更新
        int maxRetries = 3;
        int retryCount = 0;
        boolean updated = false;
        while (retryCount < maxRetries && !updated) {
            // 重新查询最新版本
            product = productsMapper.selectById(id);
            if (product == null || !merchantId.equals(product.getMerchantId())) {
                return Response.fail("商品不存在");
            }
            
            Products update = Products.builder()
                    .id(id)
                    .productName(reqVO.getProductName())
                    .mainImage(reqVO.getMainImage())
                    .description(reqVO.getDescription())
                    .detailContent(reqVO.getDetailContent())
                    .basePrice(reqVO.getBasePrice())
                    .auditStatus(0)
                    .saleStatus(0)
                    .auditRemark("")
                    .version(product.getVersion()) // 带上版本号
                    .build();

            boolean success = this.updateById(update);
            if (success) {
                updated = true;
            } else {
                retryCount++;
            }
        }
        
        if (!updated) {
            return Response.fail("更新失败，请重试");
        }
        return Response.success(true);
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
        populateCategories(page.getRecords());

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
    @Transactional
    public Response reviewProduct(Long id, FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO) {
        if (id == null || findAndUpdateReviewProductReqVO == null
                || (!Integer.valueOf(1).equals(findAndUpdateReviewProductReqVO.getAuditStatus())
                && !Integer.valueOf(2).equals(findAndUpdateReviewProductReqVO.getAuditStatus()))) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }
        boolean approved = Integer.valueOf(1).equals(findAndUpdateReviewProductReqVO.getAuditStatus());
        String remark = findAndUpdateReviewProductReqVO.getAuditRemark() == null
                ? "" : findAndUpdateReviewProductReqVO.getAuditRemark().trim();
        if (remark.length() > 255 || (!approved && remark.isBlank())) {
            return Response.fail("请填写255字以内的审核备注，驳回原因不能为空");
        }
        Products product = productsMapper.selectByIdForUpdate(id);
        if (product == null) {
            return Response.fail("商品不存在");
        }
        if (!Integer.valueOf(0).equals(product.getAuditStatus())) {
            return Response.fail("商品已审核，请刷新列表");
        }
        if (approved) {
            Categories category = categoriesMapper.selectByIdForUpdate(product.getCategoryId());
            if (category == null || (!Integer.valueOf(1).equals(category.getStatus())
                    && !Integer.valueOf(2).equals(category.getStatus()))
                    || !isParentAvailable(category)) {
                return Response.fail("商品分类不存在或已停用");
            }
            if (Integer.valueOf(2).equals(category.getStatus())) {
                if (categoriesMapper.updateById(Categories.builder()
                        .id(category.getId()).status(1).build()) != 1) {
                    throw new BizException("分类审核失败，请重试");
                }
            }
        }

        int updated = productsMapper.updateById(Products.builder()
                .id(id).auditStatus(approved ? 1 : 2).saleStatus(approved ? 1 : 0)
                .auditRemark(remark).version(product.getVersion()).build());
        if (updated != 1) {
            throw new BizException("商品审核失败，请刷新后重试");
        }
        return Response.success();
    }

    /**
     * 上下架商品
     * @param id
     * @param findAndUpdateReviewProductReqVO
     * @return
     */
    @Override
    @Transactional
    public Response updateProductShelf(Long id, FindAndUpdateReviewProductReqVO findAndUpdateReviewProductReqVO) {
        if (id == null || findAndUpdateReviewProductReqVO == null
                || (!Integer.valueOf(0).equals(findAndUpdateReviewProductReqVO.getSaleStatus())
                && !Integer.valueOf(1).equals(findAndUpdateReviewProductReqVO.getSaleStatus()))) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }
        Products product = productsMapper.selectByIdForUpdate(id);
        if (product == null) {
            return Response.fail("商品不存在");
        }
        if (Integer.valueOf(1).equals(findAndUpdateReviewProductReqVO.getSaleStatus())
                && (!Integer.valueOf(1).equals(product.getAuditStatus())
                || !isCategoryAvailable(categoriesMapper.selectById(product.getCategoryId())))) {
            return Response.fail("商品或分类尚未审核通过，不能上架");
        }
        int updated = productsMapper.updateById(Products.builder()
                .id(id).saleStatus(findAndUpdateReviewProductReqVO.getSaleStatus())
                .version(product.getVersion()).build());
        return updated == 1 ? Response.success() : Response.fail("更新失败，请重试");
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

        // 4. 使用乐观锁重试更新上架/下架状态
        int maxRetries = 3;
        int retryCount = 0;
        boolean updated = false;
        while (retryCount < maxRetries && !updated) {
            product = productsMapper.selectById(id);
            if (product == null || !merchantId.equals(product.getMerchantId())) {
                return Response.fail("商品不存在");
            }
            if (reqVO.getSaleStatus() == 1
                    && (!Integer.valueOf(1).equals(product.getAuditStatus())
                    || !isCategoryAvailable(categoriesMapper.selectById(product.getCategoryId())))) {
                return Response.fail("商品或分类尚未审核通过，不能上架");
            }

            Products update = Products.builder()
                    .id(id)
                    .saleStatus(reqVO.getSaleStatus())
                    .version(product.getVersion())
                    .build();

            boolean success = this.updateById(update);
            if (success) {
                updated = true;
            } else {
                retryCount++;
            }
        }

        if (!updated) {
            return Response.fail("更新失败，请重试");
        }
        return Response.success(true);
    }

    /**
     * 用户端分页查询商品
     * 支持父分类自动包含所有子分类的商品
     * @param reqVO
     * @return
     */
    @Override
    public Response findUserProducts(FindUserProductsReqVO reqVO) {
        // 解析分类ID：如果选的是父分类，自动包含所有子分类ID
        List<Long> categoryIds = resolveCategoryIds(reqVO.getCategoryId());

        Page<Products> page = productsMapper.findUserProducts(reqVO, categoryIds);

        FindUserProductsRspVO vos = FindUserProductsRspVO.builder()
                .records(page.getRecords())
                .total(page.getTotal())
                .pageNum((int) page.getCurrent())
                .pageSize((int) page.getSize())
                .build();

        return Response.success(vos);
    }

    /**
     * 解析分类ID：如果是父分类，返回其所有子分类ID；如果是子分类，返回自身ID
     */
    private List<Long> resolveCategoryIds(Long categoryId) {
        if (categoryId == null) {
            return null;
        }

        // 查询所有以该分类为父分类的子分类
        List<Categories> children = categoriesMapper.selectList(
                Wrappers.<Categories>lambdaQuery()
                        .eq(Categories::getParentId, categoryId)
        );

        if (children.isEmpty()) {
            // 没有子分类，说明本身就是叶子分类，直接返回自身
            return List.of(categoryId);
        }

        // 保留直接挂在父分类下的商品。
        List<Long> ids = children.stream().map(Categories::getId)
                .collect(Collectors.toCollection(ArrayList::new));
        ids.add(categoryId);
        return ids;
    }

    private boolean isParentAvailable(Categories category) {
        if (Long.valueOf(0).equals(category.getParentId())) return true;
        Categories parent = categoriesMapper.selectById(category.getParentId());
        return parent != null && Integer.valueOf(1).equals(parent.getStatus());
    }

    private boolean isCategoryAvailable(Categories category) {
        return category != null && Integer.valueOf(1).equals(category.getStatus())
                && isParentAvailable(category);
    }

    private void populateCategories(List<Products> products) {
        Set<Long> ids = products.stream().map(Products::getCategoryId)
                .filter(java.util.Objects::nonNull).collect(Collectors.toSet());
        if (ids.isEmpty()) return;
        Map<Long, Categories> categories = categoriesMapper.selectByIds(ids).stream()
                .collect(Collectors.toMap(Categories::getId, category -> category));
        Set<Long> parentIds = new HashSet<>();
        for (Categories category : categories.values()) {
            if (category.getParentId() != null && category.getParentId() != 0
                    && !categories.containsKey(category.getParentId())) {
                parentIds.add(category.getParentId());
            }
        }
        if (!parentIds.isEmpty()) {
            for (Categories parent : categoriesMapper.selectByIds(parentIds)) {
                categories.put(parent.getId(), parent);
            }
        }
        for (Products product : products) {
            Categories category = categories.get(product.getCategoryId());
            if (category != null) {
                product.setCategoryName(category.getCategoryName());
                product.setCategoryStatus(category.getStatus());
                Categories parent = categories.get(category.getParentId());
                product.setCategoryParentName(parent == null ? null : parent.getCategoryName());
            }
        }
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
        Users merchant = product.getMerchantId() == null
                ? null : usersMapper.selectById(product.getMerchantId());

        // 构建响应
        ProductDetailRspVO vos = ProductDetailRspVO.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .productCode(product.getProductCode())
                .basePrice(product.getBasePrice())
                .categoryId(product.getCategoryId())
                .merchantId(product.getMerchantId())
                .shopName(merchant == null ? null : merchant.getShopName())
                .shopLogo(merchant == null ? null : merchant.getShopLogo())
                .description(product.getDescription())
                .mainImage(product.getMainImage())
                .skus(skus)
                .build();

        return Response.success(vos);
    }

}




