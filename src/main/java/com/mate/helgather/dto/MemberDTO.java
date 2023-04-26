package com.mate.helgather.dto;

import lombok.Getter;

@Getter
public class MemberDTO {
    private Long id;
    private String userName;

    public MemberDTO(Long id, String userName) {
        this.id = id;
        this.userName = userName;
    }
}
