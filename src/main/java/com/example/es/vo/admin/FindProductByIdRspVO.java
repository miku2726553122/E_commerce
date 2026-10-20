package com.example.es.vo.admin;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindProductByIdRspVO {
    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品编码
     */
    private String productCode;

    /**
     * 商品主图
     */
    private String mainImage;

    /**
     * 商品简介
     */
    private String description;

    /**
     * 商品详情
     */
    private String detailContent;

    /**
     * 商品基础价格
     */
    private BigDecimal basePrice;

}
