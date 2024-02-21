package com.tokoonline.demo.authentication.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class JwtResponseDto implements Serializable {
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";

    public JwtResponseDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
