package com.example.es.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.es.entity.Orders;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.es.vo.admin.FindPageByOrdersReqVO;
import org.springframework.util.StringUtils;

/**
* @author 86159
* @description 针对表【orders(订单表)】的数据库操作Mapper
* @createDate 2026-10-19 10:21:26
* @Entity com.example.es.entity.Orders
*/
public interface OrdersMapper extends BaseMapper<Orders> {

    default Page<Orders> findPageByOrders(FindPageByOrdersReqVO reqVO){
        Page<Orders> page = new Page<>(reqVO.getPageNum(), reqVO.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<Orders> wrapper = Wrappers.<Orders>lambdaQuery()
                .eq(StringUtils.hasText(reqVO.getOrderNo()), Orders::getOrderNo, reqVO.getOrderNo())
                .eq(reqVO.getUserId() != null, Orders::getUserId, reqVO.getUserId())
                .eq(StringUtils.hasText(reqVO.getOrderStatus()), Orders::getOrderStatus, reqVO.getOrderStatus())
                .orderByDesc(Orders::getCreatedAt);

        return selectPage(page, wrapper);
    }


    default boolean updateOrderStatus(Long id, String orderStatus, String remark){
        LambdaUpdateWrapper<Orders> wrapper = Wrappers.<Orders>lambdaUpdate()
                .eq(Orders::getId, id)
                .set(Orders::getOrderStatus, orderStatus)
                .set(Orders::getRemark, remark);
        return update(wrapper) > 0;
    }
}




