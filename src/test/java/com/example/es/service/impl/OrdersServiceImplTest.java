package com.example.es.service.impl;

import com.example.es.auth.UserPrincipal;
import com.example.es.entity.OrderItems;
import com.example.es.entity.Orders;
import com.example.es.entity.ProductSkus;
import com.example.es.entity.Products;
import com.example.es.entity.UserAddresses;
import com.example.es.exception.BizException;
import com.example.es.mapper.*;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.admin.UpdateOrderStatusReqVO;
import com.example.es.vo.user.CreateOrderRspVO;
import com.example.es.vo.user.order.CreateOrderReqVO;
import com.example.es.vo.user.order.CancelOrderReqVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrdersServiceImplTest {
    private OrdersMapper ordersMapper;
    private OrderItemsMapper orderItemsMapper;
    private ProductsMapper productsMapper;
    private InventoriesMapper inventoriesMapper;
    private ProductSkusMapper productSkusMapper;
    private UserAddressesMapper userAddressesMapper;
    private CartsMapper cartsMapper;
    private OrdersServiceImpl service;
    private Orders order;
    private List<OrderItems> items;

    @BeforeEach
    void setUp() {
        ordersMapper = mock(OrdersMapper.class);
        orderItemsMapper = mock(OrderItemsMapper.class);
        productsMapper = mock(ProductsMapper.class);
        inventoriesMapper = mock(InventoriesMapper.class);
        productSkusMapper = mock(ProductSkusMapper.class);
        userAddressesMapper = mock(UserAddressesMapper.class);
        cartsMapper = mock(CartsMapper.class);
        service = new OrdersServiceImpl();
        ReflectionTestUtils.setField(service, "ordersMapper", ordersMapper);
        ReflectionTestUtils.setField(service, "orderItemsMapper", orderItemsMapper);
        ReflectionTestUtils.setField(service, "productsMapper", productsMapper);
        ReflectionTestUtils.setField(service, "inventoriesMapper", inventoriesMapper);
        ReflectionTestUtils.setField(service, "productSkusMapper", productSkusMapper);
        ReflectionTestUtils.setField(service, "userAddressesMapper", userAddressesMapper);
        ReflectionTestUtils.setField(service, "cartsMapper", cartsMapper);

        order = Orders.builder().id(101L).userId(12L).orderNo("ORD-101")
                .orderStatus("待付款").paymentStatus("未支付").deliveryStatus("待发货").version(0)
                .payableAmount(new BigDecimal("88.00")).build();
        items = new ArrayList<>(List.of(item(201L, 301L, 401L), item(202L, 302L, 402L)));
        authenticateAs(12L);
        when(ordersMapper.selectForUpdate(101L)).thenReturn(order);
        when(ordersMapper.updateById(any(Orders.class))).thenReturn(1);
        when(orderItemsMapper.findOrderItemsById(101L)).thenAnswer(invocation -> items);
        when(orderItemsMapper.updateById(any(OrderItems.class))).thenReturn(1);
    }

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void simulatesPaymentOnceAndMovesOrderToAwaitingShipment() {
        assertTrue(service.simulatePayment(12L, 101L).isSuccess());
        assertEquals("待发货", order.getOrderStatus());
        assertEquals("已支付", order.getPaymentStatus());
        assertNotNull(order.getPaidAt());

        LocalDateTime paidAt = order.getPaidAt();
        assertTrue(service.simulatePayment(12L, 101L).isSuccess());
        assertEquals(paidAt, order.getPaidAt());
        verify(ordersMapper, times(1)).updateById(order);
    }

    @Test
    void rejectsPaymentWhenOrderBelongsToAnotherUser() {
        authenticateAs(13L);

        assertThrows(BizException.class, () -> service.simulatePayment(13L, 101L));

        verify(ordersMapper, never()).updateById(any(Orders.class));
    }

    @Test
    void checkoutCombinesRepeatedSkusAndCreatesAnUnpaidOrderSnapshot() {
        UserAddresses address = UserAddresses.builder().id(501L).userId(12L)
                .receiverName("王先生").receiverPhone("13800000000")
                .province("浙江省").city("杭州市").district("西湖区").detailAddress("文三路1号").build();
        ProductSkus sku = ProductSkus.builder().id(401L).productId(301L).skuName("白蜡木")
                .salePrice(new BigDecimal("88.00")).status(1).build();
        Products product = Products.builder().id(301L).productName("原木杯").mainImage("cover.png")
                .saleStatus(1).auditStatus(1).build();
        when(userAddressesMapper.selectById(501L)).thenReturn(address);
        when(productSkusMapper.selectBatchIds(anyCollection())).thenReturn(List.of(sku));
        when(productsMapper.selectById(301L)).thenReturn(product);
        when(inventoriesMapper.reserve(401L, 3)).thenReturn(1);
        when(ordersMapper.insert(any(Orders.class))).thenAnswer(invocation -> {
            ((Orders) invocation.getArgument(0)).setId(601L);
            return 1;
        });
        when(orderItemsMapper.insert(any(OrderItems.class))).thenReturn(1);

        CreateOrderReqVO request = new CreateOrderReqVO();
        request.setAddressId(501L);
        request.setItems(List.of(orderItemRequest(401L, 1), orderItemRequest(401L, 2)));
        var response = service.createUserOrder(12L, request);

        assertTrue(response.isSuccess());
        var created = assertInstanceOf(CreateOrderRspVO.class, response.getData());
        assertEquals(601L, created.getId());
        assertEquals("待付款", created.getOrderStatus());
        verify(inventoriesMapper).reserve(401L, 3);
        var savedOrder = forClass(Orders.class);
        verify(ordersMapper).insert(savedOrder.capture());
        assertEquals("待付款", savedOrder.getValue().getOrderStatus());
        assertEquals("未支付", savedOrder.getValue().getPaymentStatus());
        assertEquals(new BigDecimal("264.00"), savedOrder.getValue().getPayableAmount());
        var savedItem = forClass(OrderItems.class);
        verify(orderItemsMapper).insert(savedItem.capture());
        assertEquals(3, savedItem.getValue().getQuantity());
        assertEquals(new BigDecimal("264.00"), savedItem.getValue().getTotalAmount());
        assertEquals("原木杯", savedItem.getValue().getProductName());
        assertEquals("cover.png", savedItem.getValue().getProductImage());
    }

    @Test
    void cannotShipBeforeSimulatedPayment() {
        assertThrows(BizException.class, () -> service.confirmShipment(21L, 101L));

        verify(inventoriesMapper, never()).ship(anyLong(), anyInt());
        verify(orderItemsMapper, never()).updateById(any(OrderItems.class));
    }

    @Test
    void shipsOnlyTheCurrentMerchantsItemsBeforeCompletingTheWholeOrder() {
        order.setOrderStatus("待发货");
        order.setPaymentStatus("已支付");
        when(productsMapper.selectById(301L)).thenReturn(Products.builder().id(301L).merchantId(21L).build());
        when(productsMapper.selectById(302L)).thenReturn(Products.builder().id(302L).merchantId(22L).build());
        when(inventoriesMapper.ship(401L, 1)).thenReturn(1);

        assertTrue(service.confirmShipment(21L, 101L).isSuccess());

        assertNotNull(items.get(0).getShippedAt());
        assertNull(items.get(1).getShippedAt());
        assertEquals("待发货", order.getOrderStatus());
        assertEquals("部分发货", order.getDeliveryStatus());
        verify(inventoriesMapper).ship(401L, 1);
        verify(inventoriesMapper, never()).ship(402L, 1);
        verify(orderItemsMapper).updateById(items.get(0));
    }

    @Test
    void finalMerchantShipmentAdvancesOrderAndConsumesLockedStock() {
        order.setOrderStatus("待发货");
        order.setPaymentStatus("已支付");
        items.remove(1);
        when(productsMapper.selectById(301L)).thenReturn(Products.builder().id(301L).merchantId(21L).build());
        when(inventoriesMapper.ship(401L, 1)).thenReturn(1);

        assertTrue(service.confirmShipment(21L, 101L).isSuccess());

        assertEquals("已发货", order.getOrderStatus());
        assertEquals("已发货", order.getDeliveryStatus());
        verify(inventoriesMapper).ship(401L, 1);
        assertNotNull(order.getShippedAt());
    }

    @Test
    void cancellationReleasesReservedStockAndCannotCancelAfterPayment() {
        when(inventoriesMapper.release(anyLong(), anyInt())).thenReturn(1);
        CancelOrderReqVO request = new CancelOrderReqVO();
        request.setCancelReason("不再需要");

        assertTrue(service.cancelUserOrder(12L, 101L, request).isSuccess());

        assertEquals("已取消", order.getOrderStatus());
        assertEquals("不再需要", order.getCancelReason());
        verify(inventoriesMapper).release(401L, 1);
        verify(inventoriesMapper).release(402L, 1);

        order.setOrderStatus("待发货");
        order.setPaymentStatus("已支付");
        assertThrows(BizException.class, () -> service.cancelUserOrder(12L, 101L, request));
    }

    @Test
    void adminCannotSkipPaymentAndShipmentSteps() {
        UpdateOrderStatusReqVO request = UpdateOrderStatusReqVO.builder()
                .orderStatus("已发货").remark("直接跳转").build();

        assertThrows(BizException.class, () -> service.updateOrderStatus(101L, request));

        verify(inventoriesMapper, never()).ship(anyLong(), anyInt());
    }

    private static OrderItems item(Long id, Long productId, Long skuId) {
        return OrderItems.builder().id(id).orderId(101L).productId(productId).skuId(skuId)
                .quantity(1).unitPrice(new BigDecimal("88.00"))
                .totalAmount(new BigDecimal("88.00")).build();
    }

    private static CreateOrderReqVO.OrderItemReq orderItemRequest(Long skuId, int quantity) {
        CreateOrderReqVO.OrderItemReq item = new CreateOrderReqVO.OrderItemReq();
        item.setSkuId(skuId);
        item.setQuantity(quantity);
        return item;
    }

    private static void authenticateAs(Long userId) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new UserPrincipal(userId, "demo"), null, List.of()));
    }
}
