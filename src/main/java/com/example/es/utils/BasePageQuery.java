package com.example.es.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class BasePageQuery {
    @TableField(exist = false)
    private Long current = 1L;  // 默认第一页
    @TableField(exist = false)
    private Long size = 10L;    // 默认每页 10 条
}
