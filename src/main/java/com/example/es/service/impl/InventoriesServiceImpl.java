package com.example.es.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.es.entity.Inventories;
import com.example.es.service.InventoriesService;
import com.example.es.mapper.InventoriesMapper;
import org.springframework.stereotype.Service;

/**
* @author 86159
* @description 针对表【inventories(商品库存表)】的数据库操作Service实现
* @createDate 2026-10-19 10:21:26
*/
@Service
public class InventoriesServiceImpl extends ServiceImpl<InventoriesMapper, Inventories>
    implements InventoriesService{

}




