package com.example.es.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String nickname;
    private String phone;
    private String createTime;
}
