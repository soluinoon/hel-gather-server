package com.mate.helgather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MemberLoginResponseDto {

    private Long memberId;
    private String nickname;
//    private String grantType; //jwt 인증 타입
//    private String accessToken;
//    private String refreshToken; //accessToken 재발급
}
