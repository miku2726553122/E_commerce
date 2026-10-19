package com.example.es.vo.admin;


import com.example.es.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "根据id获取用户信息 VO")
public class FindByIdRspVO {
    private long id;

    private String username;

    private String nickname;

    private String role;

    private Integer status;
}
