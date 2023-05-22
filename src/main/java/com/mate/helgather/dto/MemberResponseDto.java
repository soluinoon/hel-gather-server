package com.mate.helgather.dto;

import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;

    public MemberResponseDto(Long id) {
        this.id = id;
    }
}
