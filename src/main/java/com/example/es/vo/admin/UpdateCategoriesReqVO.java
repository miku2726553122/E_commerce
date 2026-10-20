package com.example.es.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "更新分类请求参数")
public class UpdateCategoriesReqVO {

    private String categoryName;

    private Integer status;

    private Integer sortOrder;
}
