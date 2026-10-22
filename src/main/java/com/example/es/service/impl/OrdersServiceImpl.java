package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Inventories;
import com.example.es.entity.OrderItems;
import com.example.es.entity.Orders;
import com.example.es.entity.ProductSkus;
import com.example.es.entity.Products;
import com.example.es.entity.UserAddresses;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.mapper.CartItemsMapper;
import com.example.es.mapper.CartsMapper;
import com.example.es.mapper.InventoriesMapper;
import com.example.es.mapper.OrderItemsMapper;
import com.example.es.mapper.ProductSkusMapper;
import com.example.es.mapper.ProductsMapper;
import com.example.es.mapper.UserAddressesMapper;
import com.example.es.service.OrdersService;
import com.example.es.mapper.OrdersMapper;
import com.example.es.utils.Response;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.admin.*;
import com.example.es.vo.user.order.CancelOrderReqVO;
import com.example.es.vo.user.order.CreateOrderReqVO;
import com.example.es.vo.user.CreateOrderRspVO;
import com.example.es.vo.user.UserOrderItemRspVO;
import com.example.es.vo.user.UserOrderPageRspVO;
import com.example.es.vo.user.UserOrderRspVO;
import com.example.es.vo.merchant.order.MerchantOrderDetailRspVO;
import com.example.es.vo.merchant.order.MerchantOrderRspVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
        implements OrdersService{

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

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 校验订单存在性及归属权，失败返回错误响应
     */
    private Response validateOrderOwnership(Long orderId, Long userId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return Response.fail("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            return Response.fail("无权操作该订单");
        }
        return null;
    }

    /**
     * 订单项转 VO
     */
    private UserOrderItemRspVO toOrderItemRspVO(OrderItems item) {
        return UserOrderItemRspVO.builder()
                .id(item.getId())
                .productName(item.getProductName())
                .skuName(item.getSkuName())
                .productImage(item.getProductImage())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .totalAmount(item.getTotalAmount())
                .build();
    }

    /**
     * 订单转用户订单 VO
     */
    private UserOrderRspVO toUserOrderRspVO(Orders order) {
        List<OrderItems> orderItems = orderItemsMapper.findOrderItemsById(order.getId());
        List<UserOrderItemRspVO> itemVos = orderItems.stream()
                .map(this::toOrderItemRspVO)
                .collect(Collectors.toList());

        return UserOrderRspVO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .receiverAddress(order.getReceiverAddress())
                .payableAmount(order.getPayableAmount())
                .orderStatus(order.getOrderStatus())
                .items(itemVos)
                .build();
    }

    /**
     * 判断订单项是否属于指定商家
     */
    private boolean isMerchantProduct(OrderItems item, Long merchantId) {
        Products product = productsMapper.selectById(item.getProductId());
        return product != null && merchantId.equals(product.getMerchantId());
    }


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

    /**
     * 生成订单编号（UUID 去连字符，全局唯一）
     */
    private String generateOrderNo() {
        return "ORD" + UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response createUserOrder(Long userId, CreateOrderReqVO reqVO) {
        log.info("[创建订单] 开始, userId={}, addressId={}, 商品数量={}", userId, reqVO.getAddressId(), reqVO.getItems().size());

        // 1. 获取当前登录用户
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 参数校验
        if (reqVO.getAddressId() == null || reqVO.getItems() == null || reqVO.getItems().isEmpty()) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 3. 查询收货地址
        UserAddresses address = userAddressesMapper.selectById(reqVO.getAddressId());
        if (address == null || !address.getUserId().equals(currentUserId)) {
            return Response.fail("收货地址不存在或无权使用");
        }

        // 4. 查询 SKU 信息
        List<Long> skuIds = reqVO.getItems().stream()
                .map(item -> item.getSkuId())
                .distinct()
                .collect(Collectors.toList());
        Map<Long, ProductSkus> skuMap = productSkusMapper.selectBatchIds(skuIds).stream()
                .collect(Collectors.toMap(ProductSkus::getId, s -> s));

        // 5. 校验每个 SKU 的参数
        for (var item : reqVO.getItems()) {
            ProductSkus sku = skuMap.get(item.getSkuId());
            if (sku == null) {
                return Response.fail("SKU不存在: " + item.getSkuId());
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                return Response.fail(ResponseCodeEnum.PARAM_ERROR);
            }
        }

        // 6. 锁定库存（串行，使用乐观锁重试，保证原子性）
        log.info("[创建订单] 开始锁定库存...");
        Map<Long, Inventories> lockedInventoryMap = new HashMap<>();
        for (var item : reqVO.getItems()) {
            int maxRetries = 3;
            int retryCount = 0;
            boolean locked = false;
            while (retryCount < maxRetries && !locked) {
                LambdaQueryWrapper<Inventories> invWrapper = Wrappers.<Inventories>lambdaQuery()
                        .eq(Inventories::getSkuId, item.getSkuId());
                Inventories inventory = inventoriesMapper.selectOne(invWrapper);
                if (inventory == null) {
                    return Response.fail("库存信息不存在: " + item.getSkuId());
                }
                int availableStock = inventory.getStockQuantity() - inventory.getLockedQuantity();
                if (availableStock < item.getQuantity()) {
                    return Response.fail("库存不足: " + skuMap.get(item.getSkuId()).getSkuName());
                }
                inventory.setLockedQuantity(inventory.getLockedQuantity() + item.getQuantity());
                int rows = inventoriesMapper.updateById(inventory);
                if (rows > 0) {
                    locked = true;
                    lockedInventoryMap.put(item.getSkuId(), inventory);
                    log.debug("[创建订单] SKU {} 库存锁定成功, 数量={}", item.getSkuId(), item.getQuantity());
                } else {
                    retryCount++;
                    log.warn("[创建订单] SKU {} 库存锁定冲突, 重试 {}/{}", item.getSkuId(), retryCount, maxRetries);
                }
            }
            if (!locked) {
                log.error("[创建订单] SKU {} 库存锁定失败", item.getSkuId());
                return Response.fail("库存锁定失败，请重试");
            }
        }
        log.info("[创建订单] 库存锁定完成");

        // 7. 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (var item : reqVO.getItems()) {
            ProductSkus sku = skuMap.get(item.getSkuId());
            totalAmount = totalAmount.add(sku.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // 8. 创建订单
        String orderNo = generateOrderNo();
        String receiverAddress = address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress();

        Orders order = Orders.builder()
                .orderNo(orderNo)
                .userId(currentUserId)
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .receiverAddress(receiverAddress)
                .totalAmount(totalAmount)
                .freightAmount(BigDecimal.ZERO)
                .payableAmount(totalAmount)
                .orderStatus("待付款")
                .paymentStatus("未支付")
                .deliveryStatus("待发货")
                .userRemark(reqVO.getUserRemark())
                .build();
        ordersMapper.insert(order);
        log.info("[创建订单] 订单已创建: orderNo={}, 总金额={}", orderNo, totalAmount);

        // 9. 创建订单项
        for (var item : reqVO.getItems()) {
            ProductSkus sku = skuMap.get(item.getSkuId());
            BigDecimal unitPrice = sku.getSalePrice();
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

            Products product = productsMapper.selectById(sku.getProductId());

            OrderItems orderItem = OrderItems.builder()
                    .orderId(order.getId())
                    .productId(sku.getProductId())
                    .skuId(item.getSkuId())
                    .productName(product != null ? product.getProductName() : null)
                    .skuName(sku.getSkuName())
                    .skuSpecification(sku.getSpecification())
                    .productImage(sku.getImageUrl())
                    .unitPrice(unitPrice)
                    .quantity(item.getQuantity())
                    .totalAmount(itemTotal)
                    .build();
            orderItemsMapper.insert(orderItem);
        }
        log.info("[创建订单] 订单项已创建, 共 {} 项", reqVO.getItems().size());

        // 10. 返回结果
        CreateOrderRspVO rspVO = CreateOrderRspVO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .payableAmount(order.getPayableAmount())
                .orderStatus(order.getOrderStatus())
                .build();

        log.info("[创建订单] 完成, orderNo={}", orderNo);
        return Response.success(rspVO);
    }

    @Override
    public Response getUserOrders(Long userId) {
        // 1. 获取当前登录用户
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 分页查询订单
        Page<Orders> page = new Page<>(1, 10); // 默认分页参数
        LambdaQueryWrapper<Orders> wrapper = Wrappers.<Orders>lambdaQuery()
                .eq(Orders::getUserId, currentUserId)
                .orderByDesc(Orders::getCreatedAt);
        Page<Orders> orderPage = ordersMapper.selectPage(page, wrapper);

        // 3. 转换为VO列表（并行查询订单项）
        List<UserOrderRspVO> voList = orderPage.getRecords().stream()
                .map(order -> CompletableFuture.supplyAsync(() ->
                        toUserOrderRspVO(order), threadPoolTaskExecutor))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // 4. 构建分页结果
        UserOrderPageRspVO result = UserOrderPageRspVO.builder()
                .records(voList)
                .total(orderPage.getTotal())
                .pageNum((int) orderPage.getCurrent())
                .pageSize((int) orderPage.getSize())
                .build();

        return Response.success(result);
    }

    @Override
    public Response getUserOrderDetail(Long userId, Long orderId) {
        // 1. 获取当前登录用户
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 校验订单存在性及归属权
        Response ownershipError = validateOrderOwnership(orderId, currentUserId);
        if (ownershipError != null) {
            return ownershipError;
        }

        // 3. 查询订单并构建响应
        Orders order = ordersMapper.selectById(orderId);
        return Response.success(toUserOrderRspVO(order));
    }

    /**
     * 取消用户订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response cancelUserOrder(Long userId, Long orderId, CancelOrderReqVO reqVO) {
        log.info("[取消订单] 开始, orderId={}, 取消原因={}", orderId, reqVO.getCancelReason());

        // 1. 获取当前登录用户
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 校验订单存在性及归属权
        Response ownershipError = validateOrderOwnership(orderId, currentUserId);
        if (ownershipError != null) {
            return ownershipError;
        }

        // 3. 校验订单状态（只有待付款的订单可以取消）
        // 4. 使用乐观锁更新订单状态
        int maxOrderRetries = 3;
        int orderRetryCount = 0;
        boolean orderUpdated = false;
        Orders order = null;
        while (orderRetryCount < maxOrderRetries && !orderUpdated) {
            // 重新查询最新版本
            order = ordersMapper.selectById(orderId);
            if (order == null) {
                return Response.fail("订单不存在");
            }
            if (!"待付款".equals(order.getOrderStatus())) {
                return Response.fail("只有待付款的订单可以取消");
            }
            
            order.setOrderStatus("已取消");
            order.setCancelReason(reqVO.getCancelReason());
            order.setCancelledAt(LocalDateTime.now());
            
            int rows = ordersMapper.updateById(order);
            if (rows > 0) {
                orderUpdated = true;
            } else {
                orderRetryCount++;
            }
        }
        
        if (!orderUpdated) {
            log.error("[取消订单] 订单状态更新失败, orderId={}", orderId);
            return Response.fail("取消订单失败，请重试");
        }
        log.info("[取消订单] 订单状态已更新为已取消");

        // 5. 释放锁定的库存（并行释放，使用乐观锁）
        log.info("[取消订单] 开始释放库存...");
        List<OrderItems> orderItems = orderItemsMapper.findOrderItemsById(orderId);
        List<CompletableFuture<Boolean>> releaseFutures = new ArrayList<>();
        for (OrderItems item : orderItems) {
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                int maxInvRetries = 3;
                int invRetryCount = 0;
                while (invRetryCount < maxInvRetries) {
                    try {
                        // 查询库存（包含版本号）
                        LambdaQueryWrapper<Inventories> invWrapper = Wrappers.<Inventories>lambdaQuery()
                                .eq(Inventories::getSkuId, item.getSkuId());
                        Inventories inventory = inventoriesMapper.selectOne(invWrapper);
                        if (inventory == null) {
                            return false;
                        }
                        
                        // 修改库存
                        inventory.setLockedQuantity(inventory.getLockedQuantity() - item.getQuantity());
                        
                        // 更新（乐观锁会自动在 WHERE 条件中加入 version 校验）
                        int invRows = inventoriesMapper.updateById(inventory);
                        if (invRows > 0) {
                            return true; // 更新成功
                        }
                        // 更新失败（版本冲突），重试
                        invRetryCount++;
                    } catch (Exception e) {
                        invRetryCount++;
                    }
                }
                return false; // 重试失败
            }, threadPoolTaskExecutor);
            releaseFutures.add(future);
        }

        // 等待所有库存释放完成并检查结果
        boolean allSuccess = CompletableFuture.allOf(releaseFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> releaseFutures.stream()
                        .allMatch(future -> {
                            try {
                                return future.get();
                            } catch (Exception e) {
                                return false;
                            }
                        }))
                .join();

        if (!allSuccess) {
            log.error("[取消订单] 库存释放失败, orderId={}", orderId);
            throw new RuntimeException("库存释放失败，请重试");
        }

        log.info("[取消订单] 完成, orderId={}", orderId);
        return Response.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response confirmReceipt(Long userId, Long orderId) {
        log.info("[确认收货] 开始, orderId={}", orderId);

        // 1. 获取当前登录用户
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 校验订单存在性及归属权
        Response ownershipError = validateOrderOwnership(orderId, currentUserId);
        if (ownershipError != null) {
            return ownershipError;
        }

        // 3. 校验订单状态（只有已发货的订单可以确认收货）
        // 4. 使用乐观锁更新订单状态
        int maxReceiptRetries = 3;
        int receiptRetryCount = 0;
        boolean receiptUpdated = false;
        Orders receiptOrder = null;
        while (receiptRetryCount < maxReceiptRetries && !receiptUpdated) {
            // 重新查询最新版本
            receiptOrder = ordersMapper.selectById(orderId);
            if (receiptOrder == null) {
                return Response.fail("订单不存在");
            }
            if (!"已发货".equals(receiptOrder.getOrderStatus())) {
                return Response.fail("只有已发货的订单可以确认收货");
            }
            
            receiptOrder.setOrderStatus("已完成");
            receiptOrder.setDeliveryStatus("已签收");
            receiptOrder.setCompletedAt(LocalDateTime.now());
            
            int receiptRows = ordersMapper.updateById(receiptOrder);
            if (receiptRows > 0) {
                receiptUpdated = true;
            } else {
                receiptRetryCount++;
            }
        }
        
        if (!receiptUpdated) {
            log.error("[确认收货] 订单状态更新失败, orderId={}", orderId);
            return Response.fail("确认收货失败，请重试");
        }

        log.info("[确认收货] 完成, orderId={}, 订单状态已更新为已完成", orderId);
        return Response.success(true);
    }

    @Override
    public Response getMerchantOrders(Long merchantId) {
        if (merchantId == null) {
            return Response.fail(ResponseCodeEnum.UNAUTHORIZED);
        }

        // 1. 先查询商家的所有商品 ID
        List<Long> merchantProductIds = productsMapper.selectList(
                Wrappers.<Products>lambdaQuery()
                        .eq(Products::getMerchantId, merchantId)
                        .select(Products::getId)
        ).stream().map(Products::getId).collect(Collectors.toList());

        if (merchantProductIds.isEmpty()) {
            return Response.success(new ArrayList<>());
        }

        // 2. 只查询包含商家商品的订单项
        List<OrderItems> merchantItems = orderItemsMapper.selectList(
                Wrappers.<OrderItems>lambdaQuery()
                        .in(OrderItems::getProductId, merchantProductIds)
        );

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
                .filter(item -> isMerchantProduct(item, merchantId))
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
        log.info("[确认发货] 开始, merchantId={}, orderId={}", merchantId, orderId);

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
                .anyMatch(item -> isMerchantProduct(item, merchantId));

        if (!hasMerchantProduct) {
            return Response.fail("该订单不包含您的商品");
        }

        // 确认发货（只有待发货的订单才能发货）
        if (!"待发货".equals(order.getOrderStatus())) {
            return Response.fail("订单状态不正确，无法发货");
        }

        // 使用乐观锁重试更新
        int maxRetries = 3;
        int retryCount = 0;
        boolean updated = false;
        while (retryCount < maxRetries && !updated) {
            order = this.getById(orderId);
            if (order == null) {
                return Response.fail("订单不存在");
            }
            if (!"待发货".equals(order.getOrderStatus())) {
                return Response.fail("订单状态不正确，无法发货");
            }

            order.setOrderStatus("已发货");
            order.setDeliveryStatus("已发货");
            order.setShippedAt(LocalDateTime.now());

            int rows = ordersMapper.updateById(order);
            if (rows > 0) {
                updated = true;
            } else {
                retryCount++;
            }
        }

        if (!updated) {
            log.error("[确认发货] 订单状态更新失败, orderId={}", orderId);
            return Response.fail("确认发货失败，请重试");
        }

        log.info("[确认发货] 完成, orderId={}, 订单状态已更新为已发货", orderId);
        return Response.success(true);
    }
}
