package com.example.es.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.OrderItems;
import com.example.es.entity.Orders;
import com.example.es.entity.Products;
import com.example.es.mapper.OrderItemsMapper;
import com.example.es.mapper.ProductsMapper;
import com.example.es.service.OrdersService;
import com.example.es.mapper.OrdersMapper;
import com.example.es.utils.Response;
import com.example.es.vo.admin.FindOrdersByIdRspVO;
import com.example.es.vo.admin.FindOrdersItemsByIdRspVO;
import com.example.es.vo.admin.FindPageByOrdersReqVO;
import com.example.es.vo.admin.FindProductByIdRspVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86159
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
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

        //订单是否存在
        if (ordersMapper.selectById(id) == null) {
            return Response.fail("订单不存在");
        }

        //查找订单项
        List<OrderItems> orderItems = orderItemsMapper.findOrderItemsById(id);

        //DO转VO
        List<FindOrdersItemsByIdRspVO> findOrdersItemsByIdRspVO = orderItems.stream().map(item -> {
            // 根据每个订单项的productId查询对应的商品
            Products products = productsMapper.selectById(item.getProductId());
            
            //商品DO转VO
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


        FindOrdersByIdRspVO vos=null;
        //DO转VO
        vos= FindOrdersByIdRspVO.builder()
                .id(orders.getId())
                .orderNo(orders.getOrderNo())
                .userId(orders.getUserId())
                .payableAmount(orders.getPayableAmount())
                .orderStatus(orders.getOrderStatus())
                .items(findOrdersItemsByIdRspVO)
                .build();

        return Response.success(vos);
    }
}




