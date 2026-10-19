package com.example.es.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "添加商家请求参数")
public class AddMechantsReqVO {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String nickname;

    @NotEmpty
    private String shopName;
}
