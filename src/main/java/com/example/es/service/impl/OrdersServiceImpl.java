package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.*;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.exception.BizException;
import com.example.es.mapper.*;
import com.example.es.service.OrdersService;
import com.example.es.utils.Response;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.admin.*;
import com.example.es.vo.merchant.MerchantOrderDetailRspVO;
import com.example.es.vo.merchant.MerchantOrderRspVO;
import com.example.es.vo.user.*;
import com.example.es.vo.user.order.CancelOrderReqVO;
import com.example.es.vo.user.order.CreateOrderReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private ProductsMapper productsMapper;
    @Autowired
    private ProductSkusMapper productSkusMapper;
    @Autowired
    private InventoriesMapper inventoriesMapper;
    @Autowired
    private UserAddressesMapper userAddressesMapper;
    @Autowired
    private CartsMapper cartsMapper;
    @Autowired
    private CartItemsMapper cartItemsMapper;

    private Long currentUserId() {
        Long id = SecurityUtils.getCurrentUserId();
        if (id == null) {
            throw new BizException(ResponseCodeEnum.NOT_LOGGED_IN);
        }
        return id;
    }

    private Orders requireOrder(Long id, boolean lock) {
        Orders order = lock ? ordersMapper.selectForUpdate(id) : ordersMapper.selectById(id);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        return order;
    }

    private Orders userOrder(Long id, boolean lock) {
        Long userId = currentUserId();
        Orders order = requireOrder(id, lock);
        if (!userId.equals(order.getUserId())) {
            throw new BizException("无权操作该订单");
        }
        return order;
    }

    private void saveOrder(Orders order) {
        if (ordersMapper.updateById(order) != 1) {
            throw new BizException("订单已发生变化，请刷新后重试");
        }
    }

    private List<OrderItems> requireItems(Long orderId) {
        List<OrderItems> items = orderItemsMapper.findOrderItemsById(orderId);
        if (items.isEmpty()) {
            throw new BizException("订单缺少商品明细，无法操作");
        }
        return items;
    }

    // Sort SKU locks consistently across orders; every write stays on the transaction thread.
    private Map<Long, Integer> quantities(List<OrderItems> items) {
        Map<Long, Integer> result = new TreeMap<>();
        for (OrderItems item : items) {
            if (item.getSkuId() == null || item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BizException("订单商品数量异常，无法操作");
            }
            result.merge(item.getSkuId(), item.getQuantity(), Math::addExact);
        }
        return result;
    }

    private UserOrderItemRspVO toOrderItemRspVO(OrderItems item) {
        return UserOrderItemRspVO.builder()
                .id(item.getId()).productName(item.getProductName()).skuName(item.getSkuName())
                .productImage(item.getProductImage()).unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity()).totalAmount(item.getTotalAmount())
                .shippedAt(item.getShippedAt()).build();
    }

    private UserOrderRspVO toUserOrderRspVO(Orders order) {
        return UserOrderRspVO.builder()
                .id(order.getId()).orderNo(order.getOrderNo())
                .receiverName(order.getReceiverName()).receiverPhone(order.getReceiverPhone())
                .receiverAddress(order.getReceiverAddress()).payableAmount(order.getPayableAmount())
                .orderStatus(order.getOrderStatus()).paymentStatus(order.getPaymentStatus())
                .deliveryStatus(order.getDeliveryStatus()).cancelReason(order.getCancelReason())
                .userRemark(order.getUserRemark()).createdAt(order.getCreatedAt())
                .paidAt(order.getPaidAt()).shippedAt(order.getShippedAt())
                .completedAt(order.getCompletedAt()).cancelledAt(order.getCancelledAt())
                .items(orderItemsMapper.findOrderItemsById(order.getId()).stream()
                        .map(this::toOrderItemRspVO).toList())
                .build();
    }

    private boolean isMerchantProduct(OrderItems item, Long merchantId) {
        Products product = productsMapper.selectById(item.getProductId());
        return product != null && merchantId.equals(product.getMerchantId());
    }

    @Override
    public Response findPageByOrders(FindPageByOrdersReqVO request) {
        return Response.success(ordersMapper.findPageByOrders(request));
    }

    @Override
    public Response findOrdersById(Long id) {
        Orders order = requireOrder(id, false);
        List<OrderItems> items = orderItemsMapper.findOrderItemsById(id);
        List<FindOrdersItemsByIdRspVO> itemVos = items.stream().map(item -> {
            Products product = productsMapper.selectById(item.getProductId());
            FindProductByIdRspVO productVo = product == null ? null : FindProductByIdRspVO.builder()
                    .productName(product.getProductName()).productCode(product.getProductCode())
                    .mainImage(product.getMainImage()).description(product.getDescription())
                    .detailContent(product.getDetailContent()).basePrice(product.getBasePrice()).build();
            return FindOrdersItemsByIdRspVO.builder()
                    .id(item.getId()).orderId(item.getOrderId()).productId(item.getProductId())
                    .product(productVo).skuId(item.getSkuId()).productName(item.getProductName())
                    .skuName(item.getSkuName()).skuSpecification(item.getSkuSpecification())
                    .productImage(item.getProductImage()).unitPrice(item.getUnitPrice())
                    .quantity(item.getQuantity()).totalAmount(item.getTotalAmount())
                    .shippedAt(item.getShippedAt()).build();
        }).toList();

        return Response.success(FindOrdersByIdRspVO.builder()
                .id(order.getId()).orderNo(order.getOrderNo()).userId(order.getUserId())
                .payableAmount(order.getPayableAmount()).orderStatus(order.getOrderStatus())
                .paymentStatus(order.getPaymentStatus()).deliveryStatus(order.getDeliveryStatus())
                .receiverName(order.getReceiverName()).receiverPhone(order.getReceiverPhone())
                .receiverAddress(order.getReceiverAddress()).userRemark(order.getUserRemark())
                .cancelReason(order.getCancelReason()).remark(order.getRemark())
                .createdAt(order.getCreatedAt()).paidAt(order.getPaidAt())
                .shippedAt(order.getShippedAt()).completedAt(order.getCompletedAt())
                .cancelledAt(order.getCancelledAt()).items(itemVos).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateOrderStatus(Long id, UpdateOrderStatusReqVO request) {
        Orders order = requireOrder(id, true);
        if (request == null || !StringUtils.hasText(request.getOrderStatus())) {
            throw new BizException("请选择订单状态");
        }
        if (request.getRemark() != null && request.getRemark().length() > 500) {
            throw new BizException("处理说明不能超过500字");
        }
        order.setRemark(request.getRemark());
        if (request.getOrderStatus().equals(order.getOrderStatus())) {
            saveOrder(order);
            return Response.success();
        }
        switch (request.getOrderStatus()) {
            case "待发货" -> markSimulatedPayment(order);
            case "已发货" -> shipItems(order, requireItems(id));
            case "已完成" -> completeOrder(order);
            case "已取消" -> cancelOrder(order, StringUtils.hasText(request.getRemark())
                    ? request.getRemark() : "管理员取消订单");
            default -> throw new BizException("不支持该状态流转，订单不能回退或跳过步骤");
        }
        return Response.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response createUserOrder(Long userId, CreateOrderReqVO request) {
        Long currentUserId = currentUserId();
        if (request == null || request.getAddressId() == null
                || request.getItems() == null || request.getItems().isEmpty()) {
            throw new BizException(ResponseCodeEnum.PARAM_ERROR);
        }
        if (request.getUserRemark() != null && request.getUserRemark().length() > 500) {
            throw new BizException("买家备注不能超过500字");
        }
        UserAddresses address = userAddressesMapper.selectById(request.getAddressId());
        if (address == null || !currentUserId.equals(address.getUserId())) {
            throw new BizException("收货地址不存在或无权使用");
        }
        Map<Long, Integer> quantities = new TreeMap<>();
        for (CreateOrderReqVO.OrderItemReq item : request.getItems()) {
            if (item == null || item.getSkuId() == null || item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new BizException(ResponseCodeEnum.PARAM_ERROR);
            }
            long total = (long) quantities.getOrDefault(item.getSkuId(), 0) + item.getQuantity();
            if (total > Integer.MAX_VALUE) {
                throw new BizException("购买数量过大");
            }
            quantities.put(item.getSkuId(), (int) total);
        }
        Map<Long, ProductSkus> skus = productSkusMapper.selectBatchIds(quantities.keySet()).stream()
                .collect(Collectors.toMap(ProductSkus::getId, sku -> sku));
        Map<Long, Products> products = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (var entry : quantities.entrySet()) {
            ProductSkus sku = skus.get(entry.getKey());
            if (sku == null || !Integer.valueOf(1).equals(sku.getStatus())
                    || sku.getSalePrice() == null || sku.getSalePrice().signum() < 0) {
                throw new BizException("商品规格不存在或已停用");
            }
            Products product = products.computeIfAbsent(sku.getProductId(), productsMapper::selectById);
            if (product == null || !Integer.valueOf(1).equals(product.getSaleStatus())
                    || !Integer.valueOf(1).equals(product.getAuditStatus())) {
                throw new BizException("商品已下架或尚未通过审核");
            }
            totalAmount = totalAmount.add(sku.getSalePrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }
        // Throw on failure so earlier reservations and cart changes roll back together.
        for (var entry : quantities.entrySet()) {
            if (inventoriesMapper.reserve(entry.getKey(), entry.getValue()) != 1) {
                throw new BizException("库存不足或库存不存在：" + skus.get(entry.getKey()).getSkuName());
            }
        }
        Orders order = Orders.builder()
                .orderNo("ORD" + UUID.randomUUID().toString().replace("-", ""))
                .userId(currentUserId).receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .receiverAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress())
                .totalAmount(totalAmount).freightAmount(BigDecimal.ZERO).payableAmount(totalAmount)
                .orderStatus("待付款").paymentStatus("未支付").deliveryStatus("待发货")
                .userRemark(request.getUserRemark()).version(0).build();
        if (ordersMapper.insert(order) != 1) {
            throw new BizException("创建订单失败，请重试");
        }
        for (var entry : quantities.entrySet()) {
            ProductSkus sku = skus.get(entry.getKey());
            Products product = products.get(sku.getProductId());
            OrderItems item = OrderItems.builder()
                    .orderId(order.getId()).productId(sku.getProductId()).skuId(sku.getId())
                    .productName(product.getProductName()).skuName(sku.getSkuName())
                    .skuSpecification(sku.getSpecification())
                    .productImage(StringUtils.hasText(sku.getImageUrl()) ? sku.getImageUrl() : product.getMainImage())
                    .unitPrice(sku.getSalePrice()).quantity(entry.getValue())
                    .totalAmount(sku.getSalePrice().multiply(BigDecimal.valueOf(entry.getValue()))).build();
            if (orderItemsMapper.insert(item) != 1) {
                throw new BizException("保存订单商品失败，请重试");
            }
        }
        Carts cart = cartsMapper.selectOne(Wrappers.<Carts>lambdaQuery().eq(Carts::getUserId, currentUserId));
        if (cart != null) {
            cartItemsMapper.delete(Wrappers.<CartItems>lambdaQuery()
                    .eq(CartItems::getCartId, cart.getId()).in(CartItems::getSkuId, quantities.keySet()));
        }
        return Response.success(CreateOrderRspVO.builder()
                .id(order.getId()).orderNo(order.getOrderNo()).payableAmount(order.getPayableAmount())
                .orderStatus(order.getOrderStatus()).build());
    }

    @Override
    public Response getUserOrders(Long userId, int pageNum, int pageSize) {
        Long currentUserId = currentUserId();
        Page<Orders> page = ordersMapper.selectPage(new Page<>(Math.max(1, pageNum), Math.max(1, Math.min(100, pageSize))),
                Wrappers.<Orders>lambdaQuery().eq(Orders::getUserId, currentUserId)
                        .orderByDesc(Orders::getCreatedAt).orderByDesc(Orders::getId));
        return Response.success(UserOrderPageRspVO.builder()
                .records(page.getRecords().stream().map(this::toUserOrderRspVO).toList())
                .total(page.getTotal()).pageNum((int) page.getCurrent()).pageSize((int) page.getSize()).build());
    }

    @Override
    public Response getUserOrderDetail(Long userId, Long orderId) {
        return Response.success(toUserOrderRspVO(userOrder(orderId, false)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response simulatePayment(Long userId, Long orderId) {
        Orders order = userOrder(orderId, true);
        if ("已支付".equals(order.getPaymentStatus()) && List.of("待发货", "已发货", "已完成").contains(order.getOrderStatus())) {
            return Response.success(true);
        }
        markSimulatedPayment(order);
        return Response.success(true);
    }

    private void markSimulatedPayment(Orders order) {
        if (!"待付款".equals(order.getOrderStatus()) || !"未支付".equals(order.getPaymentStatus())) {
            throw new BizException("只有待付款且未支付的订单可以模拟付款");
        }
        requireItems(order.getId());
        order.setOrderStatus("待发货");
        order.setPaymentStatus("已支付");
        order.setPaidAt(LocalDateTime.now());
        saveOrder(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response cancelUserOrder(Long userId, Long orderId, CancelOrderReqVO request) {
        Orders order = userOrder(orderId, true);
        if ("已取消".equals(order.getOrderStatus())) {
            return Response.success(true);
        }
        cancelOrder(order, request == null ? null : request.getCancelReason());
        return Response.success(true);
    }

    private void cancelOrder(Orders order, String reason) {
        if (!"待付款".equals(order.getOrderStatus()) || !"未支付".equals(order.getPaymentStatus())) {
            throw new BizException("只有待付款且未支付的订单可以取消");
        }
        if (!StringUtils.hasText(reason) || reason.length() > 255) {
            throw new BizException("请填写取消原因，最多255字");
        }
        for (var entry : quantities(requireItems(order.getId())).entrySet()) {
            if (inventoriesMapper.release(entry.getKey(), entry.getValue()) != 1) {
                throw new BizException("锁定库存异常，取消失败，请联系管理员");
            }
        }
        order.setOrderStatus("已取消");
        order.setCancelReason(reason.trim());
        order.setCancelledAt(LocalDateTime.now());
        saveOrder(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response confirmReceipt(Long userId, Long orderId) {
        Orders order = userOrder(orderId, true);
        if (!"已完成".equals(order.getOrderStatus())) {
            completeOrder(order);
        }
        return Response.success(true);
    }

    private void completeOrder(Orders order) {
        if (!"已发货".equals(order.getOrderStatus()) || !"已支付".equals(order.getPaymentStatus())
                || !"已发货".equals(order.getDeliveryStatus())) {
            throw new BizException("只有已全部发货的订单可以确认收货");
        }
        order.setOrderStatus("已完成");
        order.setDeliveryStatus("已签收");
        order.setCompletedAt(LocalDateTime.now());
        saveOrder(order);
    }

    private boolean canShip(Orders order, List<OrderItems> items) {
        return "待发货".equals(order.getOrderStatus()) && "已支付".equals(order.getPaymentStatus())
                && items.stream().anyMatch(item -> item.getShippedAt() == null);
    }

    private String merchantDeliveryStatus(Orders order, List<OrderItems> items) {
        if (List.of("已发货", "已完成").contains(order.getOrderStatus())) {
            return order.getDeliveryStatus();
        }
        long shipped = items.stream().filter(item -> item.getShippedAt() != null).count();
        return shipped == 0 ? "待发货" : shipped == items.size() ? "已发货" : "部分发货";
    }

    @Override
    public Response getMerchantOrders(Long merchantId) {
        if (merchantId == null) {
            throw new BizException(ResponseCodeEnum.UNAUTHORIZED);
        }
        List<Long> productIds = productsMapper.selectList(Wrappers.<Products>lambdaQuery()
                .eq(Products::getMerchantId, merchantId).select(Products::getId))
                .stream().map(Products::getId).toList();
        if (productIds.isEmpty()) {
            return Response.success(List.of());
        }
        List<OrderItems> items = orderItemsMapper.selectList(
                Wrappers.<OrderItems>lambdaQuery().in(OrderItems::getProductId, productIds));
        if (items.isEmpty()) {
            return Response.success(List.of());
        }
        Map<Long, List<OrderItems>> byOrder = items.stream().collect(Collectors.groupingBy(OrderItems::getOrderId));
        List<Orders> orders = ordersMapper.selectList(Wrappers.<Orders>lambdaQuery().in(Orders::getId, byOrder.keySet())
                .orderByDesc(Orders::getCreatedAt).orderByDesc(Orders::getId));
        return Response.success(orders.stream().map(order -> {
            List<OrderItems> merchantItems = byOrder.get(order.getId());
            return MerchantOrderRspVO.builder()
                    .orderId(order.getId()).orderNumber(order.getOrderNo()).orderStatus(order.getOrderStatus())
                    .deliveryStatus(merchantDeliveryStatus(order, merchantItems)).paymentStatus(order.getPaymentStatus())
                    .globalDeliveryStatus(order.getDeliveryStatus())
                    .canShip(canShip(order, merchantItems)).merchantAmount(merchantAmount(merchantItems))
                    .createdAt(order.getCreatedAt()).build();
        }).toList());
    }

    private BigDecimal merchantAmount(List<OrderItems> items) {
        return items.stream().map(OrderItems::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItems> merchantItems(Long merchantId, List<OrderItems> items) {
        if (merchantId == null) {
            throw new BizException(ResponseCodeEnum.UNAUTHORIZED);
        }
        List<OrderItems> result = items.stream().filter(item -> isMerchantProduct(item, merchantId)).toList();
        if (result.isEmpty()) {
            throw new BizException("该订单不包含您的商品");
        }
        return result;
    }

    @Override
    public Response getMerchantOrderDetail(Long merchantId, Long orderId) {
        Orders order = requireOrder(orderId, false);
        List<OrderItems> items = merchantItems(merchantId, requireItems(orderId));
        LocalDateTime merchantShippedAt = items.stream().map(OrderItems::getShippedAt).filter(Objects::nonNull)
                .max(Comparator.naturalOrder()).orElse(order.getShippedAt());
        return Response.success(MerchantOrderDetailRspVO.builder()
                .orderId(order.getId()).orderNumber(order.getOrderNo()).orderStatus(order.getOrderStatus())
                .deliveryStatus(merchantDeliveryStatus(order, items)).paymentStatus(order.getPaymentStatus())
                .globalDeliveryStatus(order.getDeliveryStatus())
                .canShip(canShip(order, items)).items(items).merchantAmount(merchantAmount(items))
                .receiverName(order.getReceiverName()).receiverPhone(order.getReceiverPhone())
                .receiverAddress(order.getReceiverAddress()).userRemark(order.getUserRemark())
                .createdAt(order.getCreatedAt()).paidAt(order.getPaidAt()).shippedAt(merchantShippedAt)
                .completedAt(order.getCompletedAt()).cancelledAt(order.getCancelledAt())
                .cancelReason(order.getCancelReason()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response confirmShipment(Long merchantId, Long orderId) {
        Orders order = requireOrder(orderId, true);
        List<OrderItems> items = merchantItems(merchantId, requireItems(orderId));
        if (!items.stream().allMatch(item -> item.getShippedAt() != null)) {
            shipItems(order, items);
        }
        return Response.success(true);
    }

    private void shipItems(Orders order, List<OrderItems> items) {
        if (!"待发货".equals(order.getOrderStatus()) || !"已支付".equals(order.getPaymentStatus())) {
            throw new BizException("只有已模拟付款、待发货的订单可以发货");
        }
        List<OrderItems> pending = items.stream().filter(item -> item.getShippedAt() == null).toList();
        if (pending.isEmpty()) {
            throw new BizException("所选商品已发货，请刷新订单");
        }
        for (var entry : quantities(pending).entrySet()) {
            if (inventoriesMapper.ship(entry.getKey(), entry.getValue()) != 1) {
                throw new BizException("锁定库存异常，发货失败，请联系管理员");
            }
        }
        LocalDateTime now = LocalDateTime.now();
        for (OrderItems item : pending) {
            item.setShippedAt(now);
            if (orderItemsMapper.updateById(item) != 1) {
                throw new BizException("商品发货记录保存失败，请重试");
            }
        }
        boolean allShipped = requireItems(order.getId()).stream().allMatch(item -> item.getShippedAt() != null);
        order.setDeliveryStatus(allShipped ? "已发货" : "部分发货");
        if (allShipped) {
            order.setOrderStatus("已发货");
            order.setShippedAt(now);
        }
        saveOrder(order);
    }
}
