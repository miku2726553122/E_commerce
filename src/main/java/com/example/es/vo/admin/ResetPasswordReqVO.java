package com.example.es.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "重置用户密码请求参数")
public class ResetPasswordReqVO {

    @NotBlank
    @Size(min = 6, max = 20)
    private String newPassword;
}