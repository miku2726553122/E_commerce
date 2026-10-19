package com.example.es.vo.admin;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddMerchantsRspVO {
    private Long id;

    private String username;

    private String nickname;

    private String shopName;
}
