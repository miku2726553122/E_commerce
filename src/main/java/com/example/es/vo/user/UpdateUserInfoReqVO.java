package com.example.es.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "更新用户信息请求VO")
public class UpdateUserInfoReqVO {

    private String nickname;
    private String realName;
    private String phone;
    private String avatar;
}
