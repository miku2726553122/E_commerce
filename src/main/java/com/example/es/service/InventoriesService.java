package com.example.es.service;

import com.example.es.entity.Inventories;
import com.baomidou.mybatisplus.spring.service.IService;
import com.example.es.utils.Response;
import com.example.es.vo.merchant.skuinventory.UpdateInventoryReqVO;

/**
* @author 86159
* @description 针对表【inventories(商品库存表)】的数据库操作Service
* @createDate 2026-10-19 10:21:26
*/
public interface InventoriesService extends IService<Inventories> {

    /**
     * 查询商家库存列表
     */
    Response getMerchantInventories(Long merchantId);

    /**
     * 修改库存
     */
    Response updateInventory(Long merchantId, Long skuId, UpdateInventoryReqVO reqVO);

}
