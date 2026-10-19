package com.example.es.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "查询所有用户 VO")
public class FindAllRoleReqVO {

    private String role;

    private Integer status;

    private String keyword;

    private Integer pageNum;

    private Integer pageSize;

}
