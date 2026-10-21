package com.example.es.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "UpdatePasswordReqVO")
public class UpdatePasswordReqVO {
    @Schema(description = "oldPassword")
    @NotBlank
    private String oldPassword;
    @Schema(description = "newPassword")
    @NotBlank
    private String newPassword;
}
