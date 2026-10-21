package com.example.es.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.CartItems;
import com.example.es.entity.Carts;
import com.example.es.entity.ProductSkus;
import com.example.es.entity.Products;
import com.example.es.entity.Inventories;
import com.example.es.enums.ResponseCodeEnum;
import com.example.es.mapper.CartItemsMapper;
import com.example.es.mapper.CartsMapper;
import com.example.es.mapper.InventoriesMapper;
import com.example.es.mapper.ProductSkusMapper;
import com.example.es.mapper.ProductsMapper;
import com.example.es.mapper.UsersMapper;
import com.example.es.service.CartsService;
import com.example.es.utils.Response;
import com.example.es.utils.SecurityUtils;
import com.example.es.vo.user.AddCartItemReqVO;
import com.example.es.vo.user.AddCartItemRspVO;
import com.example.es.vo.user.CartItemRspVO;
import com.example.es.vo.user.CartRspVO;
import com.example.es.vo.user.UpdateCartItemReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author 86159
* @description 针对表【carts(购物车表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class CartsServiceImpl extends ServiceImpl<CartsMapper, Carts>
    implements CartsService{

    @Autowired
    private CartsMapper cartsMapper;

    @Autowired
    private CartItemsMapper cartItemsMapper;

    @Autowired
    private ProductsMapper productsMapper;

    @Autowired
    private ProductSkusMapper productSkusMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private InventoriesMapper inventoriesMapper;

    /**
     * 获取购物车
     */

    @Override
    public Response getCart() {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 根据用户名查询用户，获取 userId
        Long userId = usersMapper.findByUsername(username).getId();

        // 3. 根据 userId 查询购物车
        LambdaQueryWrapper<Carts> cartWrapper = Wrappers.<Carts>lambdaQuery()
                .eq(Carts::getUserId, userId);
        Carts cart = cartsMapper.selectOne(cartWrapper);

        if (cart == null) {
            // 购物车不存在，返回空购物车
            CartRspVO emptyCart = CartRspVO.builder()
                    .id(null)
                    .items(Collections.emptyList())
                    .totalAmount(BigDecimal.ZERO)
                    .build();
            return Response.success(emptyCart);
        }

        // 4. 查询购物车明细
        LambdaQueryWrapper<CartItems> itemsWrapper = Wrappers.<CartItems>lambdaQuery()
                .eq(CartItems::getCartId, cart.getId());
        List<CartItems> cartItems = cartItemsMapper.selectList(itemsWrapper);

        if (cartItems.isEmpty()) {
            CartRspVO emptyCart = CartRspVO.builder()
                    .id(cart.getId())
                    .items(Collections.emptyList())
                    .totalAmount(BigDecimal.ZERO)
                    .build();
            return Response.success(emptyCart);
        }

        // 5. 批量查询商品和 SKU 信息，构建 Map 映射（避免在 Stream lambda 中逐个查询）
        List<Long> productIds = cartItems.stream()
                .map(CartItems::getProductId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> skuIds = cartItems.stream()
                .map(CartItems::getSkuId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Products> productMap = productsMapper.selectBatchIds(productIds).stream()
                .collect(Collectors.toMap(Products::getId, p -> p));
        Map<Long, ProductSkus> skuMap = productSkusMapper.selectBatchIds(skuIds).stream()
                .collect(Collectors.toMap(ProductSkus::getId, s -> s));

        // 6. 组装购物车明细 VO，并计算选中商品总金额
        List<CartItemRspVO> itemVos = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItems item : cartItems) {
            Products product = productMap.get(item.getProductId());
            ProductSkus sku = skuMap.get(item.getSkuId());

            CartItemRspVO itemVo = CartItemRspVO.builder()
                    .id(item.getId())
                    .productId(item.getProductId())
                    .productName(product != null ? product.getProductName() : null)
                    .mainImage(product != null ? product.getMainImage() : null)
                    .skuId(item.getSkuId())
                    .skuName(sku != null ? sku.getSkuName() : null)
                    .skuImageUrl(sku != null ? sku.getImageUrl() : null)
                    .salePrice(sku != null ? sku.getSalePrice() : null)
                    .quantity(item.getQuantity())
                    .selected(item.getSelected())
                    .build();
            itemVos.add(itemVo);

            // 累加选中商品的金额
            if (item.getSelected() != null && item.getSelected() == 1
                    && sku != null && sku.getSalePrice() != null) {
                totalAmount = totalAmount.add(
                        sku.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                );
            }
        }

        // 7. 组装购物车 VO
        CartRspVO cartRspVO = CartRspVO.builder()
                .id(cart.getId())
                .items(itemVos)
                .totalAmount(totalAmount)
                .build();

        return Response.success(cartRspVO);
    }

    /**
     * 添加购物车
     */
    @Override
    public Response addToCart(AddCartItemReqVO reqVO) {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 参数校验
        Long skuId = reqVO.getSkuId();
        Integer quantity = reqVO.getQuantity();
        if (skuId == null || quantity == null || quantity <= 0) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        // 3. 校验 SKU 是否存在
        ProductSkus sku = productSkusMapper.selectById(skuId);
        if (sku == null) {
            return Response.fail(ResponseCodeEnum.NOT_FOUND);
        }

        // 4. 校验库存（实际库存 - 锁定库存 >= 请求数量）
        LambdaQueryWrapper<Inventories> invWrapper = Wrappers.<Inventories>lambdaQuery()
                .eq(Inventories::getSkuId, skuId);
        Inventories inventory = inventoriesMapper.selectOne(invWrapper);
        if (inventory == null) {
            return Response.fail(ResponseCodeEnum.NOT_FOUND);
        }
        int availableStock = inventory.getStockQuantity() - inventory.getLockedQuantity();
        if (availableStock < quantity) {
            return Response.fail("库存不足");
        }

        // 5. 查找或创建用户购物车
        Long userId = usersMapper.findByUsername(username).getId();
        LambdaQueryWrapper<Carts> cartWrapper = Wrappers.<Carts>lambdaQuery()
                .eq(Carts::getUserId, userId);
        Carts cart = cartsMapper.selectOne(cartWrapper);
        if (cart == null) {
            cart = Carts.builder()
                    .userId(userId)
                    .build();
            cartsMapper.insert(cart);
        }

        // 6. 检查购物车中是否已存在该 SKU 的明细
        LambdaQueryWrapper<CartItems> itemWrapper = Wrappers.<CartItems>lambdaQuery()
                .eq(CartItems::getCartId, cart.getId())
                .eq(CartItems::getSkuId, skuId);
        CartItems existingItem = cartItemsMapper.selectOne(itemWrapper);

        CartItems cartItem;
        if (existingItem != null) {
            // 已存在，累加数量
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemsMapper.updateById(existingItem);
            cartItem = existingItem;
        } else {
            // 不存在，新增明细
            cartItem = CartItems.builder()
                    .cartId(cart.getId())
                    .productId(sku.getProductId())
                    .skuId(skuId)
                    .quantity(quantity)
                    .selected(1)
                    .build();
            cartItemsMapper.insert(cartItem);
        }

        // 7. 返回结果
        AddCartItemRspVO rspVO = AddCartItemRspVO.builder()
                .id(cartItem.getId())
                .skuId(cartItem.getSkuId())
                .quantity(cartItem.getQuantity())
                .build();

        return Response.success(rspVO);
    }

    @Override
    public Response updateCartItem(Long id, UpdateCartItemReqVO reqVO) {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 查找购物车明细
        CartItems cartItem = cartItemsMapper.selectById(id);
        if (cartItem == null) {
            return Response.fail(ResponseCodeEnum.NOT_FOUND);
        }

        // 3. 校验是否属于当前用户的购物车
        Long userId = usersMapper.findByUsername(username).getId();
        LambdaQueryWrapper<Carts> cartWrapper = Wrappers.<Carts>lambdaQuery()
                .eq(Carts::getUserId, userId);
        Carts cart = cartsMapper.selectOne(cartWrapper);
        if (cart == null || !cart.getId().equals(cartItem.getCartId())) {
            return Response.fail(ResponseCodeEnum.NOT_FOUND);
        }

        // 4. 构建更新条件，使用 LambdaUpdateWrapper 支持部分字段更新
        LambdaUpdateWrapper<CartItems> updateWrapper = Wrappers.<CartItems>lambdaUpdate()
                .eq(CartItems::getId, id);

        boolean needUpdate = false;

        // 更新数量
        if (reqVO.getQuantity() != null) {
            if (reqVO.getQuantity() <= 0) {
                return Response.fail(ResponseCodeEnum.PARAM_ERROR);
            }
            // 校验库存
            LambdaQueryWrapper<Inventories> invWrapper = Wrappers.<Inventories>lambdaQuery()
                    .eq(Inventories::getSkuId, cartItem.getSkuId());
            Inventories inventory = inventoriesMapper.selectOne(invWrapper);
            if (inventory == null) {
                return Response.fail(ResponseCodeEnum.NOT_FOUND);
            }
            int availableStock = inventory.getStockQuantity() - inventory.getLockedQuantity();
            if (availableStock < reqVO.getQuantity()) {
                return Response.fail("库存不足");
            }
            updateWrapper.set(CartItems::getQuantity, reqVO.getQuantity());
            needUpdate = true;
        }

        // 更新选中状态
        if (reqVO.getSelected() != null) {
            updateWrapper.set(CartItems::getSelected, reqVO.getSelected() ? 1 : 0);
            needUpdate = true;
        }

        if (!needUpdate) {
            return Response.fail(ResponseCodeEnum.PARAM_ERROR);
        }

        boolean result = cartItemsMapper.update(null, updateWrapper) > 0;
        return Response.success(result);
    }

    @Override
    public Response deleteCartItem(Long id) {
        // 1. 获取当前登录用户
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            return Response.fail(ResponseCodeEnum.NOT_LOGGED_IN);
        }

        // 2. 查找购物车明细
        CartItems cartItem = cartItemsMapper.selectById(id);
        if (cartItem == null) {
            return Response.fail(ResponseCodeEnum.NOT_FOUND);
        }

        // 3. 校验是否属于当前用户的购物车
        Long userId = usersMapper.findByUsername(username).getId();
        LambdaQueryWrapper<Carts> cartWrapper = Wrappers.<Carts>lambdaQuery()
                .eq(Carts::getUserId, userId);
        Carts cart = cartsMapper.selectOne(cartWrapper);
        if (cart == null || !cart.getId().equals(cartItem.getCartId())) {
            return Response.fail(ResponseCodeEnum.NOT_FOUND);
        }

        // 4. 删除购物车明细
        boolean result = cartItemsMapper.deleteById(id) > 0;
        return Response.success(result);
    }
}




