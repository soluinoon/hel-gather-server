package com.mate.helgather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {
    private String grantType; //jwt 인증 타입
    private String accessToken;
    private String refreshToken; //accessToken 재발급
}