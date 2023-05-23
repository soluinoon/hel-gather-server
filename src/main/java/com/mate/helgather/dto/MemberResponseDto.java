package com.mate.helgather.dto;

import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String nickname;

    public MemberResponseDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
