package com.example.es.service.impl;

import com.example.es.entity.Products;
import com.example.es.mapper.ProductsMapper;
import com.example.es.vo.merchant.UpdateMerchantProductsReqVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

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
}
