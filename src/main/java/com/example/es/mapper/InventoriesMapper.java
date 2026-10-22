package com.example.es.mapper;

import com.example.es.entity.Inventories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
* @author 86159
* @description 针对表【inventories(商品库存表)】的数据库操作Mapper
* @createDate 2026-10-19 10:21:26
* @Entity com.example.es.entity.Inventories
*/
public interface InventoriesMapper extends BaseMapper<Inventories> {

    @Update("""
            UPDATE inventories
            SET locked_quantity = locked_quantity + #{quantity}, version = version + 1
            WHERE sku_id = #{skuId} AND stock_quantity - locked_quantity >= #{quantity}
            """)
    int reserve(@Param("skuId") Long skuId, @Param("quantity") int quantity);

    @Update("""
            UPDATE inventories
            SET locked_quantity = locked_quantity - #{quantity}, version = version + 1
            WHERE sku_id = #{skuId} AND locked_quantity >= #{quantity}
            """)
    int release(@Param("skuId") Long skuId, @Param("quantity") int quantity);

    @Update("""
            UPDATE inventories
            SET stock_quantity = stock_quantity - #{quantity},
                locked_quantity = locked_quantity - #{quantity}, version = version + 1
            WHERE sku_id = #{skuId} AND locked_quantity >= #{quantity} AND stock_quantity >= #{quantity}
            """)
    int ship(@Param("skuId") Long skuId, @Param("quantity") int quantity);
}




