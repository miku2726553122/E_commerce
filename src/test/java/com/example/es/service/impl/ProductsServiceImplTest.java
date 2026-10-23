package com.example.es.service.impl;

import com.example.es.entity.Products;
import com.example.es.entity.ProductSkus;
import com.example.es.entity.Users;
import com.example.es.mapper.ProductSkusMapper;
import com.example.es.mapper.ProductsMapper;
import com.example.es.mapper.UsersMapper;
import com.example.es.vo.merchant.UpdateMerchantProductsReqVO;
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
    private ProductsServiceImpl service;

    @BeforeEach
    void setUp() {
        productsMapper = mock(ProductsMapper.class);
        service = spy(new ProductsServiceImpl());
        ReflectionTestUtils.setField(service, "productsMapper", productsMapper);
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
