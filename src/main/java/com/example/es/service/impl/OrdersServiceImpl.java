package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.*;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.mapper.*;
import com.example.es.service.OrdersService;
import com.example.es.utils.Response;
import com.example.es.vo.admin.*;
import com.example.es.vo.merchant.order.MerchantOrderDetailRspVO;
import com.example.es.vo.merchant.order.MerchantOrderRspVO;
import com.example.es.vo.user.order.*;
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

    @Autowired
    private UserAddressesMapper userAddressesMapper;

    @Autowired
    private ProductSkusMapper productSkusMapper;

    @Autowired
    private InventoriesMapper inventoriesMapper;

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

    @Override
    public Response createUserOrder(Long userId, CreateOrderReqVO reqVO) {
        if (userId == null || reqVO == null || reqVO.getItems() == null || reqVO.getItems().isEmpty()) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 查询收货地址
        UserAddresses address = userAddressesMapper.selectById(reqVO.getAddressId());
        if (address == null) {
            return Response.fail("收货地址不存在");
        }
        if (!userId.equals(address.getUserId())) {
            return Response.fail("收货地址不属于当前用户");
        }

        // 验证商品SKU并计算总价
        List<OrderItems> orderItemsList = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CreateOrderReqVO.OrderItemReq itemReq : reqVO.getItems()) {
            // 查询SKU
            ProductSkus sku = productSkusMapper.selectById(itemReq.getSkuId());
            if (sku == null) {
                return Response.fail("SKU不存在: " + itemReq.getSkuId());
            }
            if (sku.getStatus() == null || sku.getStatus() != 1) {
                return Response.fail("SKU已停用: " + sku.getSkuName());
            }

            // 查询商品
            Products product = productsMapper.selectById(sku.getProductId());
            if (product == null) {
                return Response.fail("商品不存在");
            }
            if (!"1".equals(product.getSaleStatus())) {
                return Response.fail("商品未上架: " + product.getProductName());
            }

            // 检查库存
            Inventories inventory = inventoriesMapper.selectOne(
                Wrappers.<Inventories>lambdaQuery()
                    .eq(Inventories::getSkuId, sku.getId())
            );
            if (inventory == null || inventory.getStockQuantity() < itemReq.getQuantity()) {
                return Response.fail("库存不足: " + sku.getSkuName());
            }

            // 计算金额
            BigDecimal itemTotal = sku.getSalePrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            // 创建订单项
            OrderItems orderItem = OrderItems.builder()
                .productId(product.getId())
                .skuId(sku.getId())
                .productName(product.getProductName())
                .skuName(sku.getSkuName())
                .skuSpecification(sku.getSpecification() != null ? sku.getSpecification().toString() : "")
                .productImage(sku.getImageUrl() != null ? sku.getImageUrl() : product.getMainImage())
                .unitPrice(sku.getSalePrice())
                .quantity(itemReq.getQuantity())
                .totalAmount(itemTotal)
                .createdAt(LocalDateTime.now())
                .build();
            orderItemsList.add(orderItem);
        }

        // 计算运费（简化版，固定运费）
        BigDecimal freightAmount = BigDecimal.valueOf(10.00);
        BigDecimal payableAmount = totalAmount.add(freightAmount);

        // 生成订单编号
        String orderNo = "ORD" + System.currentTimeMillis() + userId;

        // 创建订单
        Orders order = Orders.builder()
            .orderNo(orderNo)
            .userId(userId)
            .receiverName(address.getReceiverName())
            .receiverPhone(address.getReceiverPhone())
            .receiverAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress())
            .totalAmount(totalAmount)
            .freightAmount(freightAmount)
            .payableAmount(payableAmount)
            .orderStatus("0")  // 待付款
            .paymentStatus("0")  // 未支付
            .deliveryStatus("0")  // 待发货
            .userRemark(reqVO.getUserRemark())
            .createdAt(LocalDateTime.now())
            .build();

        boolean savedOrder = this.save(order);
        if (!savedOrder) {
            return Response.fail("创建订单失败");
        }

        // 保存订单明细
        for (OrderItems item : orderItemsList) {
            item.setOrderId(order.getId());
            orderItemsMapper.insert(item);

            // 扣减库存
            Inventories inventory = inventoriesMapper.selectOne(
                Wrappers.<Inventories>lambdaQuery()
                    .eq(Inventories::getSkuId, item.getSkuId())
            );
            if (inventory != null) {
                inventory.setStockQuantity(inventory.getStockQuantity() - item.getQuantity());
                inventoriesMapper.updateById(inventory);
            }
        }

        return Response.success(order.getId());
    }

    @Override
    public Response getUserOrders(Long userId) {
        if (userId == null) {
            return Response.fail(ResponseCodeEnum.UNAUTHORIZED);
        }

        List<Orders> orders = this.list(
            Wrappers.<Orders>lambdaQuery()
                .eq(Orders::getUserId, userId)
                .orderByDesc(Orders::getCreatedAt)
        );

        List<UserOrderRspVO> result = orders.stream()
            .map(order -> UserOrderRspVO.builder()
                .orderId(order.getId())
                .orderNo(order.getOrderNo())
                .totalAmount(order.getTotalAmount())
                .freightAmount(order.getFreightAmount())
                .payableAmount(order.getPayableAmount())
                .orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus())
                .deliveryStatus(order.getDeliveryStatus())
                .createdAt(order.getCreatedAt())
                .build())
            .collect(Collectors.toList());

        return Response.success(result);
    }

    @Override
    public Response getUserOrderDetail(Long userId, Long orderId) {
        if (userId == null || orderId == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Orders order = this.getById(orderId);
        if (order == null) {
            return Response.fail("订单不存在");
        }
        if (!userId.equals(order.getUserId())) {
            return Response.fail("无权查看该订单");
        }

        List<OrderItems> items = orderItemsMapper.selectList(
            Wrappers.<OrderItems>lambdaQuery()
                .eq(OrderItems::getOrderId, orderId)
        );

        UserOrderDetailRspVO result = UserOrderDetailRspVO.builder()
            .orderId(order.getId())
            .orderNo(order.getOrderNo())
            .receiverName(order.getReceiverName())
            .receiverPhone(order.getReceiverPhone())
            .receiverAddress(order.getReceiverAddress())
            .totalAmount(order.getTotalAmount())
            .freightAmount(order.getFreightAmount())
            .payableAmount(order.getPayableAmount())
            .orderStatus(order.getOrderStatus())
            .paymentStatus(order.getPaymentStatus())
            .deliveryStatus(order.getDeliveryStatus())
            .userRemark(order.getUserRemark())
            .items(items)
            .createdAt(order.getCreatedAt())
            .paidAt(order.getPaidAt())
            .shippedAt(order.getShippedAt())
            .completedAt(order.getCompletedAt())
            .build();

        return Response.success(result);
    }

    @Override
    public Response cancelUserOrder(Long userId, Long orderId, CancelOrderReqVO reqVO) {
        if (userId == null || orderId == null || reqVO == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Orders order = this.getById(orderId);
        if (order == null) {
            return Response.fail("订单不存在");
        }
        if (!userId.equals(order.getUserId())) {
            return Response.fail("无权操作该订单");
        }

        // 只有待付款和待发货状态可以取消
        if (!"0".equals(order.getOrderStatus()) && !"1".equals(order.getOrderStatus())) {
            return Response.fail("当前订单状态不允许取消");
        }

        // 更新订单状态
        Orders update = Orders.builder()
            .id(orderId)
            .orderStatus("4")  // 已取消
            .cancelReason(reqVO.getCancelReason())
            .cancelledAt(LocalDateTime.now())
            .build();

        boolean updated = this.updateById(update);

        // 恢复库存
        if (updated) {
            List<OrderItems> items = orderItemsMapper.selectList(
                Wrappers.<OrderItems>lambdaQuery()
                    .eq(OrderItems::getOrderId, orderId)
            );

            for (OrderItems item : items) {
                Inventories inventory = inventoriesMapper.selectOne(
                    Wrappers.<Inventories>lambdaQuery()
                        .eq(Inventories::getSkuId, item.getSkuId())
                );
                if (inventory != null) {
                    inventory.setStockQuantity(inventory.getStockQuantity() + item.getQuantity());
                    inventoriesMapper.updateById(inventory);
                }
            }
        }

        return Response.success(updated);
    }

    @Override
    public Response confirmReceipt(Long userId, Long orderId) {
        if (userId == null || orderId == null) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        Orders order = this.getById(orderId);
        if (order == null) {
            return Response.fail("订单不存在");
        }
        if (!userId.equals(order.getUserId())) {
            return Response.fail("无权操作该订单");
        }

        // 只有已发货状态可以确认收货
        if (!"2".equals(order.getOrderStatus())) {
            return Response.fail("当前订单状态不允许确认收货");
        }

        // 更新订单状态
        Orders update = Orders.builder()
            .id(orderId)
            .orderStatus("3")  // 已完成
            .deliveryStatus("2")  // 已签收
            .completedAt(LocalDateTime.now())
            .build();

        boolean updated = this.updateById(update);
        return Response.success(updated);
    }
}
