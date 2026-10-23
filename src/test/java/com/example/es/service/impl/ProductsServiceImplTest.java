package com.example.es.service.impl;

import com.example.es.entity.Products;
import com.example.es.entity.ProductSkus;
import com.example.es.entity.Users;
import com.example.es.entity.Categories;
import com.example.es.exception.BizException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.es.mapper.CategoriesMapper;
import com.example.es.mapper.ProductSkusMapper;
import com.example.es.mapper.ProductsMapper;
import com.example.es.mapper.UsersMapper;
import com.example.es.vo.merchant.UpdateMerchantProductsReqVO;
import com.example.es.vo.merchant.CreateMerchantProductReqVO;
import com.example.es.vo.merchant.UpdateMerchantProductStatusReqVO;
import com.example.es.vo.admin.FindAllProductsReqVO;
import com.example.es.vo.admin.FindAllProductsRspVO;
import com.example.es.vo.admin.FindAndUpdateReviewProductReqVO;
import com.example.es.vo.user.ProductDetailRspVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductsServiceImplTest {
    private ProductsMapper productsMapper;
    private CategoriesMapper categoriesMapper;
    private ProductsServiceImpl service;

    @BeforeEach
    void setUp() {
        productsMapper = mock(ProductsMapper.class);
        categoriesMapper = mock(CategoriesMapper.class);
        service = spy(new ProductsServiceImpl());
        ReflectionTestUtils.setField(service, "productsMapper", productsMapper);
        ReflectionTestUtils.setField(service, "categoriesMapper", categoriesMapper);
        doReturn(true).when(service).updateById(any(Products.class));
        when(productsMapper.selectById(101L)).thenReturn(Products.builder()
                .id(101L)
                .merchantId(12L)
                .mainImage("https://example.test/original.png")
                .version(3)
                .build());
    }

    @Test
    void updatesCoverWithoutRequiringOtherFields() {
        var request = UpdateMerchantProductsReqVO.builder()
                .mainImage("https://example.test/new-cover.png")
                .build();

        assertTrue(service.updateMerchantProduct(12L, 101L, request).isSuccess());

        var update = ArgumentCaptor.forClass(Products.class);
        verify(service).updateById(update.capture());
        assertEquals("https://example.test/new-cover.png", update.getValue().getMainImage());
        assertEquals(101L, update.getValue().getId());
        assertEquals(3, update.getValue().getVersion());
        assertNull(update.getValue().getProductName());
        assertEquals(0, update.getValue().getAuditStatus());
        assertEquals(0, update.getValue().getSaleStatus());
    }

    @Test
    void removesCoverWithAnEmptyString() {
        var request = UpdateMerchantProductsReqVO.builder().mainImage("").build();

        assertTrue(service.updateMerchantProduct(12L, 101L, request).isSuccess());

        var update = ArgumentCaptor.forClass(Products.class);
        verify(service).updateById(update.capture());
        assertEquals("", update.getValue().getMainImage());
    }

    @Test
    void leavesCoverOutWhenItWasNotProvided() {
        var request = UpdateMerchantProductsReqVO.builder().productName("Updated product").build();

        assertTrue(service.updateMerchantProduct(12L, 101L, request).isSuccess());

        var update = ArgumentCaptor.forClass(Products.class);
        verify(service).updateById(update.capture());
        assertNull(update.getValue().getMainImage());
    }

    @Test
    void createsPendingCategoryAndProductTogether() {
        when(categoriesMapper.selectOne(any())).thenReturn(null);
        doAnswer(invocation -> {
            Categories category = invocation.getArgument(0);
            category.setId(88L);
            return 1;
        }).when(categoriesMapper).insert(any(Categories.class));
        doReturn(true).when(service).save(any(Products.class));

        var request = CreateMerchantProductReqVO.builder()
                .newCategoryName("  宠物用品  ")
                .categoryParentId(0L)
                .productName("猫咪餐垫")
                .productCode("PET-101")
                .basePrice(new BigDecimal("39.90"))
                .build();

        var response = service.createMerchantProduct(12L, request);

        assertTrue(response.isSuccess());
        var categoryCaptor = org.mockito.ArgumentCaptor.forClass(Categories.class);
        verify(categoriesMapper).insert(categoryCaptor.capture());
        assertEquals("宠物用品", categoryCaptor.getValue().getCategoryName());
        assertEquals(2, categoryCaptor.getValue().getStatus());
        var productCaptor = org.mockito.ArgumentCaptor.forClass(Products.class);
        verify(service).save(productCaptor.capture());
        assertEquals(88L, productCaptor.getValue().getCategoryId());
        assertEquals(0, productCaptor.getValue().getAuditStatus());
        assertEquals(0, productCaptor.getValue().getSaleStatus());
        assertEquals("猫咪餐垫", productCaptor.getValue().getProductName());
        verify(categoriesMapper, never()).updateById(any(Categories.class));
    }

    @Test
    void rejectsNewCategoryWhenTheCategoryAlreadyExists() {
        when(categoriesMapper.selectOne(any())).thenReturn(Categories.builder()
                .id(88L).parentId(0L).categoryName("宠物用品").status(1).build());
        var request = CreateMerchantProductReqVO.builder()
                .newCategoryName("宠物用品")
                .productName("猫咪餐垫")
                .productCode("PET-101")
                .basePrice(new BigDecimal("39.90"))
                .build();

        assertFalse(service.createMerchantProduct(12L, request).isSuccess());
        verify(service, never()).save(any(Products.class));
    }

    @Test
    void reusesPendingApplicationInsteadOfCreatingAnotherCategory() {
        when(categoriesMapper.selectOne(any())).thenReturn(Categories.builder()
                .id(88L).parentId(0L).categoryName("宠物用品").status(2).build());
        doReturn(true).when(service).save(any(Products.class));

        assertTrue(service.createMerchantProduct(12L, categoryRequest()).isSuccess());
        verify(categoriesMapper, never()).insert(any(Categories.class));
        var product = ArgumentCaptor.forClass(Products.class);
        verify(service).save(product.capture());
        assertEquals(88L, product.getValue().getCategoryId());
        assertEquals(0, product.getValue().getAuditStatus());
    }

    @Test
    void rejectsPendingCategoryIdsAndInvalidParentsFromDirectRequests() {
        when(categoriesMapper.selectById(88L)).thenReturn(Categories.builder()
                .id(88L).parentId(0L).status(2).build());
        var request = categoryRequest();
        request.setNewCategoryName(null);
        request.setCategoryId(88L);
        assertFalse(service.createMerchantProduct(12L, request).isSuccess());

        request.setCategoryId(null);
        request.setNewCategoryName("宠物用品");
        request.setCategoryParentId(9L);
        when(categoriesMapper.selectByIdForUpdate(9L)).thenReturn(Categories.builder()
                .id(9L).parentId(1L).status(1).build());
        assertFalse(service.createMerchantProduct(12L, request).isSuccess());
        verify(service, never()).save(any(Products.class));
        verify(categoriesMapper, never()).insert(any(Categories.class));
    }

    @Test
    void rejectsAmbiguousOrEmptyCategoryChoices() {
        var request = categoryRequest();
        request.setCategoryId(1L);
        assertFalse(service.createMerchantProduct(12L, request).isSuccess());
        request.setNewCategoryName(null);
        request.setCategoryId(null);
        assertFalse(service.createMerchantProduct(12L, request).isSuccess());
        verify(service, never()).save(any(Products.class));
    }

    @Test
    void failedProductCreationRaisesAnExceptionToRollBackItsCategoryApplication() {
        when(categoriesMapper.selectOne(any())).thenReturn(null);
        when(categoriesMapper.insert(any(Categories.class))).thenReturn(1);
        doReturn(false).when(service).save(any(Products.class));
        assertThrows(BizException.class, () -> service.createMerchantProduct(12L, categoryRequest()));
    }

    private CreateMerchantProductReqVO categoryRequest() {
        return CreateMerchantProductReqVO.builder()
                .newCategoryName("宠物用品").categoryParentId(0L)
                .productName("猫咪餐垫").productCode("PET-101")
                .basePrice(new BigDecimal("39.90")).build();
    }

    @Test
    void approvingProductActivatesRequestedCategoryAndListsProductAtomically() {
        when(productsMapper.selectByIdForUpdate(101L)).thenReturn(Products.builder()
                .id(101L).categoryId(88L).auditStatus(0).saleStatus(0).version(4).build());
        when(categoriesMapper.selectByIdForUpdate(88L)).thenReturn(Categories.builder()
                .id(88L).parentId(0L).categoryName("宠物用品").status(2).build());
        when(categoriesMapper.updateById(any(Categories.class))).thenReturn(1);
        when(productsMapper.updateById(any(Products.class))).thenReturn(1);

        var response = service.reviewProduct(101L, FindAndUpdateReviewProductReqVO.builder()
                .auditStatus(1).auditRemark("分类和商品信息符合要求").build());

        assertTrue(response.isSuccess());
        var categoryUpdate = org.mockito.ArgumentCaptor.forClass(Categories.class);
        verify(categoriesMapper).updateById(categoryUpdate.capture());
        assertEquals(1, categoryUpdate.getValue().getStatus());
        var productUpdate = org.mockito.ArgumentCaptor.forClass(Products.class);
        verify(productsMapper).updateById(productUpdate.capture());
        assertEquals(1, productUpdate.getValue().getAuditStatus());
        assertEquals(1, productUpdate.getValue().getSaleStatus());
        assertEquals("分类和商品信息符合要求", productUpdate.getValue().getAuditRemark());
        assertEquals(4, productUpdate.getValue().getVersion());
    }

    @Test
    void rejectingProductLeavesRequestedCategoryUnpublishedAndRequiresReason() {
        when(productsMapper.selectByIdForUpdate(101L)).thenReturn(Products.builder()
                .id(101L).categoryId(88L).auditStatus(0).saleStatus(0).version(4).build());

        var missingReason = service.reviewProduct(101L, FindAndUpdateReviewProductReqVO.builder()
                .auditStatus(2).build());
        assertFalse(missingReason.isSuccess());
        verify(productsMapper, never()).updateById(any(Products.class));

        when(productsMapper.updateById(any(Products.class))).thenReturn(1);
        var rejected = service.reviewProduct(101L, FindAndUpdateReviewProductReqVO.builder()
                .auditStatus(2).auditRemark("请补充商品信息").build());
        assertTrue(rejected.isSuccess());
        verify(categoriesMapper, never()).updateById(any(Categories.class));
        var productUpdate = org.mockito.ArgumentCaptor.forClass(Products.class);
        verify(productsMapper).updateById(productUpdate.capture());
        assertEquals(2, productUpdate.getValue().getAuditStatus());
        assertEquals(0, productUpdate.getValue().getSaleStatus());
    }

    @Test
    void failedProductReviewRaisesAnExceptionToRollBackCategoryActivation() {
        when(productsMapper.selectByIdForUpdate(101L)).thenReturn(Products.builder()
                .id(101L).categoryId(88L).auditStatus(0).version(4).build());
        when(categoriesMapper.selectByIdForUpdate(88L)).thenReturn(Categories.builder()
                .id(88L).parentId(0L).status(2).build());
        when(categoriesMapper.updateById(any(Categories.class))).thenReturn(1);
        when(productsMapper.updateById(any(Products.class))).thenReturn(0);

        assertThrows(BizException.class, () -> service.reviewProduct(101L,
                FindAndUpdateReviewProductReqVO.builder().auditStatus(1).build()));
    }

    @Test
    void disabledParentAndAlreadyReviewedProductsCannotBeApproved() {
        Products product = Products.builder()
                .id(101L).categoryId(88L).auditStatus(0).version(4).build();
        when(productsMapper.selectByIdForUpdate(101L)).thenReturn(product);
        when(categoriesMapper.selectByIdForUpdate(88L)).thenReturn(Categories.builder()
                .id(88L).parentId(1L).status(2).build());
        when(categoriesMapper.selectById(1L)).thenReturn(Categories.builder()
                .id(1L).parentId(0L).status(0).build());
        var request = FindAndUpdateReviewProductReqVO.builder().auditStatus(1).build();
        assertFalse(service.reviewProduct(101L, request).isSuccess());

        product.setAuditStatus(1);
        assertFalse(service.reviewProduct(101L, request).isSuccess());
        verify(productsMapper, never()).updateById(any(Products.class));
        verify(categoriesMapper, never()).updateById(any(Categories.class));
    }

    @Test
    void pendingAndRejectedProductsCannotBeListedByEitherRole() {
        Products product = Products.builder().id(101L).merchantId(12L)
                .categoryId(88L).auditStatus(0).saleStatus(0).version(4).build();
        when(productsMapper.selectByIdForUpdate(101L)).thenReturn(product);
        when(productsMapper.selectById(101L)).thenReturn(product);
        var adminRequest = FindAndUpdateReviewProductReqVO.builder().saleStatus(1).build();
        var merchantRequest = UpdateMerchantProductStatusReqVO.builder().saleStatus(1).build();
        assertFalse(service.updateProductShelf(101L, adminRequest).isSuccess());
        assertFalse(service.updateMerchantProductStatus(12L, 101L, merchantRequest).isSuccess());
        product.setAuditStatus(2);
        assertFalse(service.updateProductShelf(101L, adminRequest).isSuccess());
        assertFalse(service.updateMerchantProductStatus(12L, 101L, merchantRequest).isSuccess());
        verify(productsMapper, never()).updateById(any(Products.class));
        verify(service, never()).updateById(any(Products.class));
    }

    @Test
    void adminProductListIncludesCategoryNamesAndPendingStatus() {
        Products product = Products.builder().id(101L).categoryId(88L).build();
        Page<Products> page = new Page<>(1, 10);
        page.setRecords(List.of(product));
        page.setTotal(1);
        when(productsMapper.findAllProducts(any())).thenReturn(page);
        when(categoriesMapper.selectByIds(java.util.Set.of(88L))).thenReturn(List.of(Categories.builder()
                .id(88L).parentId(1L).categoryName("宠物用品").status(2).build()));
        when(categoriesMapper.selectByIds(java.util.Set.of(1L))).thenReturn(List.of(Categories.builder()
                .id(1L).parentId(0L).categoryName("生活用品").status(1).build()));

        var response = service.findAllProducts(new FindAllProductsReqVO());
        assertTrue(response.isSuccess());
        var result = assertInstanceOf(FindAllProductsRspVO.class, response.getData());
        assertEquals("宠物用品", result.getRecords().get(0).getCategoryName());
        assertEquals(2, result.getRecords().get(0).getCategoryStatus());
        assertEquals("生活用品", result.getRecords().get(0).getCategoryParentName());
    }

    @Test
    void rejectsChangesToAnotherMerchantsCover() {
        var request = UpdateMerchantProductsReqVO.builder()
                .mainImage("https://example.test/new-cover.png")
                .build();

        assertFalse(service.updateMerchantProduct(99L, 101L, request).isSuccess());

        verify(service, never()).updateById(any(Products.class));
    }

    @Test
    void productDetailIncludesTheOwningShopAndProductIdentifiers() {
        var usersMapper = mock(UsersMapper.class);
        var skusMapper = mock(ProductSkusMapper.class);
        ReflectionTestUtils.setField(service, "usersMapper", usersMapper);
        ReflectionTestUtils.setField(service, "productSkusMapper", skusMapper);
        when(productsMapper.findProductById(101L)).thenReturn(Products.builder()
                .id(101L).productName("Cup").productCode("CUP-101")
                .merchantId(12L).categoryId(5L).basePrice(new BigDecimal("88.00"))
                .build());
        when(usersMapper.selectById(12L)).thenReturn(Users.builder()
                .id(12L).shopName("Cup Shop").shopLogo("https://example.test/shop.png")
                .passwordHash("private-password-hash").phone("13800138000")
                .build());
        var skus = List.of(ProductSkus.builder().id(301L).skuCode("CUP-WHITE").build());
        when(skusMapper.findByProductId(101L)).thenReturn(skus);

        var response = service.findProductDetail(101L);

        assertTrue(response.isSuccess());
        var detail = assertInstanceOf(ProductDetailRspVO.class, response.getData());
        assertEquals("CUP-101", detail.getProductCode());
        assertEquals(new BigDecimal("88.00"), detail.getBasePrice());
        assertEquals(5L, detail.getCategoryId());
        assertEquals(12L, detail.getMerchantId());
        assertEquals("Cup Shop", detail.getShopName());
        assertEquals("https://example.test/shop.png", detail.getShopLogo());
        assertEquals(skus, detail.getSkus());
        verify(usersMapper).selectById(12L);
    }

    @Test
    void productDetailRemainsAvailableWhenTheShopRecordIsMissing() {
        var usersMapper = mock(UsersMapper.class);
        var skusMapper = mock(ProductSkusMapper.class);
        ReflectionTestUtils.setField(service, "usersMapper", usersMapper);
        ReflectionTestUtils.setField(service, "productSkusMapper", skusMapper);
        when(productsMapper.findProductById(101L)).thenReturn(Products.builder()
                .id(101L).productCode("CUP-101").merchantId(12L).build());
        when(skusMapper.findByProductId(101L)).thenReturn(List.of());

        var response = service.findProductDetail(101L);

        assertTrue(response.isSuccess());
        var detail = assertInstanceOf(ProductDetailRspVO.class, response.getData());
        assertEquals("CUP-101", detail.getProductCode());
        assertEquals(12L, detail.getMerchantId());
        assertNull(detail.getShopName());
        assertNull(detail.getShopLogo());
    }
}
