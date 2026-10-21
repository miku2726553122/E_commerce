package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.OrderItems;
import com.example.es.entity.Orders;
import com.example.es.entity.Products;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.mapper.OrderItemsMapper;
import com.example.es.mapper.ProductsMapper;
import com.example.es.service.OrdersService;
import com.example.es.mapper.OrdersMapper;
import com.example.es.utils.Response;
import com.example.es.vo.admin.*;
import com.example.es.vo.merchant.order.MerchantOrderDetailRspVO;
import com.example.es.vo.merchant.order.MerchantOrderRspVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService{

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private ProductsMapper productsMapper;

    @Override
    public Response findPageByOrders(FindPageByOrdersReqVO findPageByOrdersReqVO) {
        Page<Orders> page = ordersMapper.findPageByOrders(findPageByOrdersReqVO);
        return Response.success(page);
    }

    @Override
    public Response findOrdersById(Long id) {
        Orders orders = ordersMapper.selectById(id);

        if (orders == null) {
            return Response.fail("订单不存在");
        }

        List<OrderItems> orderItems = orderItemsMapper.findOrderItemsById(id);

        List<Long> productIds = orderItems.stream()
                .map(OrderItems::getProductId)
                .distinct()
                .collect(Collectors.toList());

        final Map<Long, Products> productMap;
        if (!productIds.isEmpty()) {
            List<Products> products = productsMapper.selectBatchIds(productIds);
            productMap = products.stream()
                    .collect(Collectors.toMap(Products::getId, p -> p));
        } else {
            productMap = new HashMap<>();
        }

        List<FindOrdersItemsByIdRspVO> findOrdersItemsByIdRspVO = orderItems.stream().map(item -> {
            Products products = productMap.get(item.getProductId());

            FindProductByIdRspVO findProductByIdRspVO = null;
            if (products != null) {
                findProductByIdRspVO = FindProductByIdRspVO.builder()
                        .productName(products.getProductName())
                        .productCode(products.getProductCode())
                        .mainImage(products.getMainImage())
                        .description(products.getDescription())
                        .detailContent(products.getDetailContent())
                        .basePrice(products.getBasePrice())
                        .build();
            }

            return FindOrdersItemsByIdRspVO.builder()
                    .id(item.getId())
                    .orderId(item.getOrderId())
                    .productId(item.getProductId())
                    .product(findProductByIdRspVO)
                    .skuId(item.getSkuId())
                    .productName(item.getProductName())
                    .skuName(item.getSkuName())
                    .skuSpecification(item.getSkuSpecification())
                    .productImage(item.getProductImage())
                    .unitPrice(item.getUnitPrice())
                    .quantity(item.getQuantity())
                    .totalAmount(item.getTotalAmount())
                    .build();
        }).collect(Collectors.toList());

        FindOrdersByIdRspVO vos = FindOrdersByIdRspVO.builder()
                .id(orders.getId())
                .orderNo(orders.getOrderNo())
                .userId(orders.getUserId())
                .payableAmount(orders.getPayableAmount())
                .orderStatus(orders.getOrderStatus())
                .items(findOrdersItemsByIdRspVO)
                .build();

        return Response.success(vos);
    }

    @Override
    public Response updateOrderStatus(Long id, UpdateOrderStatusReqVO updateOrderStatusReqVO) {
        if (ordersMapper.selectById(id) == null) {
            return Response.fail("订单不存在");
        }

        boolean result = ordersMapper.updateOrderStatus(id, updateOrderStatusReqVO.getOrderStatus(), updateOrderStatusReqVO.getRemark());
        return result ? Response.success() : Response.fail("更新订单状态失败");
    }

    @Override
    public Response getMerchantOrders(Long merchantId) {
        if (merchantId == null) {
            return Response.fail(ResponseCodeEnum.UNAUTHORIZED);
        }

        List<OrderItems> items = orderItemsMapper.selectList(null);

        List<OrderItems> merchantItems = items.stream()
                .filter(item -> {
                    Products product = productsMapper.selectById(item.getProductId());
                    return product != null && merchantId.equals(product.getMerchantId());
                })
                .collect(Collectors.toList());

        if (merchantItems.isEmpty()) {
            return Response.success(new ArrayList<>());
        }

        Map<Long, List<OrderItems>> orderItemsMap = merchantItems.stream()
                .collect(Collectors.groupingBy(OrderItems::getOrderId));

        List<Long> orderIds = new ArrayList<>(orderItemsMap.keySet());
        List<Orders> orders = this.listByIds(orderIds);

        List<MerchantOrderRspVO> result = orders.stream()
                .map(order -> {
                    List<OrderItems> orderItems = orderItemsMap.get(order.getId());
                    BigDecimal merchantAmount = orderItems.stream()
                            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return MerchantOrderRspVO.builder()
                            .orderId(order.getId())
                            .orderNumber(order.getOrderNo())
                            .orderStatus(order.getOrderStatus())
                            .deliveryStatus(order.getDeliveryStatus())
                            .merchantAmount(merchantAmount)
                            .createdAt(order.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return Response.success(result);
    }

    @Override
    public Response getMerchantOrderDetail(Long merchantId, Long orderId) {
        if (merchantId == null || orderId == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Orders order = this.getById(orderId);
        if (order == null) {
            return Response.fail("订单不存在");
        }

        List<OrderItems> allItems = orderItemsMapper.selectList(
                Wrappers.<OrderItems>lambdaQuery()
                        .eq(OrderItems::getOrderId, orderId)
        );

        List<OrderItems> merchantItems = allItems.stream()
                .filter(item -> {
                    Products product = productsMapper.selectById(item.getProductId());
                    return product != null && merchantId.equals(product.getMerchantId());
                })
                .collect(Collectors.toList());

        if (merchantItems.isEmpty()) {
            return Response.fail("该订单不包含您的商品");
        }

        BigDecimal merchantAmount = merchantItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        MerchantOrderDetailRspVO result = MerchantOrderDetailRspVO.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNo())
                .orderStatus(order.getOrderStatus())
                .deliveryStatus(order.getDeliveryStatus())
                .items(merchantItems)
                .merchantAmount(merchantAmount)
                .createdAt(order.getCreatedAt())
                .shippedAt(order.getShippedAt())
                .build();

        return Response.success(result);
    }

    @Override
    public Response confirmShipment(Long merchantId, Long orderId) {
        if (merchantId == null || orderId == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Orders order = this.getById(orderId);
        if (order == null) {
            return Response.fail("订单不存在");
        }

        List<OrderItems> allItems = orderItemsMapper.selectList(
                Wrappers.<OrderItems>lambdaQuery()
                        .eq(OrderItems::getOrderId, orderId)
        );

        boolean hasMerchantProduct = allItems.stream()
                .anyMatch(item -> {
                    Products product = productsMapper.selectById(item.getProductId());
                    return product != null && merchantId.equals(product.getMerchantId());
                });

        if (!hasMerchantProduct) {
            return Response.fail("该订单不包含您的商品");
        }

        // 检查订单状态
        if ("3".equals(order.getOrderStatus()) ||
                "4".equals(order.getOrderStatus())) {
            return Response.fail("订单已完成或已取消，无法发货");
        }

        Orders update = Orders.builder()
                .id(orderId)
                .deliveryStatus("1")
                .orderStatus("2")
                .shippedAt(LocalDateTime.now())
                .build();

        boolean updated = this.updateById(update);
        return Response.success(updated);
    }
}
