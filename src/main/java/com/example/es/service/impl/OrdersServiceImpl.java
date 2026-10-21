package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.CartItems;
import com.example.es.entity.Carts;
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
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.mapper.OrderItemsMapper;
import com.example.es.mapper.ProductSkusMapper;
import com.example.es.mapper.ProductsMapper;
import com.example.es.mapper.UserAddressesMapper;
import com.example.es.mapper.UsersMapper;
import com.example.es.service.OrdersService;
import com.example.es.mapper.OrdersMapper;
import com.example.es.utils.Response;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.admin.*;
import com.example.es.vo.user.CancelOrderReqVO;
import com.example.es.vo.user.CreateOrderReqVO;
import com.example.es.vo.user.CreateOrderRspVO;
import com.example.es.vo.user.UserOrderItemRspVO;
import com.example.es.vo.user.UserOrderPageReqVO;
import com.example.es.vo.user.UserOrderRspVO;
import com.example.es.vo.merchant.order.MerchantOrderDetailRspVO;
import com.example.es.vo.merchant.order.MerchantOrderRspVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
    private ProductSkusMapper productSkusMapper;

    @Autowired
    private InventoriesMapper inventoriesMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UserAddressesMapper userAddressesMapper;

    @Autowired
    private CartsMapper cartsMapper;

    @Autowired
    private CartItemsMapper cartItemsMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


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
     * 生成订单编号
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        long random = (long) (Math.random() * 10000);
        return "ORD" + timestamp + String.format("%04d", random);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response createOrder(CreateOrderReqVO reqVO) {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 参数校验
        if (reqVO.getAddressId() == null || reqVO.getItems() == null || reqVO.getItems().isEmpty()) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 3. 获取用户ID
        Long userId = usersMapper.findByUsername(username).getId();

        // 4. 查询收货地址
        UserAddresses address = userAddressesMapper.selectById(reqVO.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) {
            return Response.fail("收货地址不存在或无权使用");
        }

        // 5. 查询SKU信息并校验库存（并行校验）
        List<Long> skuIds = reqVO.getItems().stream()
                .map(item -> item.getSkuId())
                .distinct()
                .collect(Collectors.toList());
        Map<Long, ProductSkus> skuMap = productSkusMapper.selectBatchIds(skuIds).stream()
                .collect(Collectors.toMap(ProductSkus::getId, s -> s));

        // 并行校验每个SKU的库存
        List<CompletableFuture<Response>> inventoryCheckFutures = new ArrayList<>();
        for (var item : reqVO.getItems()) {
            CompletableFuture<Response> future = CompletableFuture.supplyAsync(() -> {
                ProductSkus sku = skuMap.get(item.getSkuId());
                if (sku == null) {
                    return Response.fail("SKU不存在: " + item.getSkuId());
                }
                if (item.getQuantity() == null || item.getQuantity() <= 0) {
                    return Response.fail(ResponseCodeEnum.PARAM_ERROR);
                }

                // 查询库存
                LambdaQueryWrapper<Inventories> invWrapper = Wrappers.<Inventories>lambdaQuery()
                        .eq(Inventories::getSkuId, item.getSkuId());
                Inventories inventory = inventoriesMapper.selectOne(invWrapper);
                if (inventory == null) {
                    return Response.fail("库存信息不存在: " + item.getSkuId());
                }
                int availableStock = inventory.getStockQuantity() - inventory.getLockedQuantity();
                if (availableStock < item.getQuantity()) {
                    return Response.fail("库存不足: " + sku.getSkuName());
                }
                return Response.success();
            }, threadPoolTaskExecutor);
            inventoryCheckFutures.add(future);
        }

        // 等待所有库存校验完成
        for (CompletableFuture<Response> future : inventoryCheckFutures) {
            Response checkResult = future.join();
            if (!checkResult.isSuccess()) {
                return checkResult;
            }
        }

        // 6. 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (var item : reqVO.getItems()) {
            ProductSkus sku = skuMap.get(item.getSkuId());
            totalAmount = totalAmount.add(sku.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        // 7. 创建订单
        String orderNo = generateOrderNo();
        String receiverAddress = address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress();

        Orders order = Orders.builder()
                .orderNo(orderNo)
                .userId(userId)
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

        // 8. 创建订单项并锁定库存
        for (var item : reqVO.getItems()) {
            ProductSkus sku = skuMap.get(item.getSkuId());
            BigDecimal unitPrice = sku.getSalePrice();
            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

            // 查询商品信息
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

            // 锁定库存
            LambdaQueryWrapper<Inventories> invWrapper = Wrappers.<Inventories>lambdaQuery()
                    .eq(Inventories::getSkuId, item.getSkuId());
            Inventories inventory = inventoriesMapper.selectOne(invWrapper);
            inventory.setLockedQuantity(inventory.getLockedQuantity() + item.getQuantity());
            inventoriesMapper.updateById(inventory);
        }

        // 9. 返回结果
        CreateOrderRspVO rspVO = CreateOrderRspVO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .payableAmount(order.getPayableAmount())
                .orderStatus(order.getOrderStatus())
                .build();

        return Response.success(rspVO);
    }

    @Override
    public Response getUserOrders(UserOrderPageReqVO reqVO) {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 获取用户ID
        Long userId = usersMapper.findByUsername(username).getId();

        // 3. 分页查询订单
        Page<Orders> page = new Page<>(reqVO.getPageNum(), reqVO.getPageSize());
        LambdaQueryWrapper<Orders> wrapper = Wrappers.<Orders>lambdaQuery()
                .eq(Orders::getUserId, userId)
                .eq(reqVO.getOrderStatus() != null, Orders::getOrderStatus, reqVO.getOrderStatus())
                .orderByDesc(Orders::getCreatedAt);
        Page<Orders> orderPage = ordersMapper.selectPage(page, wrapper);

        // 4. 转换为VO列表（并行查询订单项）
        List<UserOrderRspVO> voList = orderPage.getRecords().stream()
                .map(order -> CompletableFuture.supplyAsync(() -> {
                    // 查询订单项
                    List<OrderItems> orderItems = orderItemsMapper.findOrderItemsById(order.getId());
                    List<UserOrderItemRspVO> itemVos = orderItems.stream().map(item ->
                            UserOrderItemRspVO.builder()
                                    .id(item.getId())
                                    .productName(item.getProductName())
                                    .skuName(item.getSkuName())
                                    .productImage(item.getProductImage())
                                    .unitPrice(item.getUnitPrice())
                                    .quantity(item.getQuantity())
                                    .totalAmount(item.getTotalAmount())
                                    .build()
                    ).collect(Collectors.toList());

                    return UserOrderRspVO.builder()
                            .id(order.getId())
                            .orderNo(order.getOrderNo())
                            .receiverAddress(order.getReceiverAddress())
                            .payableAmount(order.getPayableAmount())
                            .orderStatus(order.getOrderStatus())
                            .items(itemVos)
                            .build();
                }, threadPoolTaskExecutor))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // 5. 构建分页结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", voList);
        result.put("total", orderPage.getTotal());

        return Response.success(result);
    }

    @Override
    public Response getUserOrderById(Long id) {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 查询订单
        Orders order = ordersMapper.selectById(id);
        if (order == null) {
            return Response.fail("订单不存在");
        }

        // 3. 校验是否属于当前用户
        Long userId = usersMapper.findByUsername(username).getId();
        if (!order.getUserId().equals(userId)) {
            return Response.fail("无权查看该订单");
        }

        // 4. 查询订单项
        List<OrderItems> orderItems = orderItemsMapper.findOrderItemsById(id);
        List<UserOrderItemRspVO> itemVos = orderItems.stream().map(item ->
                UserOrderItemRspVO.builder()
                        .id(item.getId())
                        .productName(item.getProductName())
                        .skuName(item.getSkuName())
                        .productImage(item.getProductImage())
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .totalAmount(item.getTotalAmount())
                        .build()
        ).collect(Collectors.toList());

        // 5. 构建响应
        UserOrderRspVO vo = UserOrderRspVO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .receiverAddress(order.getReceiverAddress())
                .payableAmount(order.getPayableAmount())
                .orderStatus(order.getOrderStatus())
                .items(itemVos)
                .build();

        return Response.success(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response cancelOrder(Long id, CancelOrderReqVO reqVO) {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 查询订单
        Orders order = ordersMapper.selectById(id);
        if (order == null) {
            return Response.fail("订单不存在");
        }

        // 3. 校验是否属于当前用户
        Long userId = usersMapper.findByUsername(username).getId();
        if (!order.getUserId().equals(userId)) {
            return Response.fail("无权操作该订单");
        }

        // 4. 校验订单状态（只有待付款的订单可以取消）
        if (!"待付款".equals(order.getOrderStatus())) {
            return Response.fail("只有待付款的订单可以取消");
        }

        // 5. 更新订单状态
        order.setOrderStatus("已取消");
        order.setCancelReason(reqVO.getReason());
        order.setCancelledAt(LocalDateTime.now());
        ordersMapper.updateById(order);

        // 6. 释放锁定的库存（并行释放）
        List<OrderItems> orderItems = orderItemsMapper.findOrderItemsById(id);
        List<CompletableFuture<Void>> releaseFutures = new ArrayList<>();
        for (OrderItems item : orderItems) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                LambdaQueryWrapper<Inventories> invWrapper = Wrappers.<Inventories>lambdaQuery()
                        .eq(Inventories::getSkuId, item.getSkuId());
                Inventories inventory = inventoriesMapper.selectOne(invWrapper);
                if (inventory != null) {
                    inventory.setLockedQuantity(inventory.getLockedQuantity() - item.getQuantity());
                    inventoriesMapper.updateById(inventory);
                }
            }, threadPoolTaskExecutor);
            releaseFutures.add(future);
        }

        // 等待所有库存释放完成
        CompletableFuture.allOf(releaseFutures.toArray(new CompletableFuture[0])).join();

        return Response.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response receiveOrder(Long id) {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 查询订单
        Orders order = ordersMapper.selectById(id);
        if (order == null) {
            return Response.fail("订单不存在");
        }

        // 3. 校验是否属于当前用户
        Long userId = usersMapper.findByUsername(username).getId();
        if (!order.getUserId().equals(userId)) {
            return Response.fail("无权操作该订单");
        }

        // 4. 校验订单状态（只有已发货的订单可以确认收货）
        if (!"已发货".equals(order.getOrderStatus())) {
            return Response.fail("只有已发货的订单可以确认收货");
        }

        // 5. 更新订单状态
        order.setOrderStatus("已完成");
        order.setDeliveryStatus("已签收");
        order.setCompletedAt(LocalDateTime.now());
        ordersMapper.updateById(order);

        return Response.success(true);
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
