package com.mate.helgather.dto;

import lombok.Getter;

@Getter
public class MemberDto {
    private Long id;
    private String userName;

    public MemberDto(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
