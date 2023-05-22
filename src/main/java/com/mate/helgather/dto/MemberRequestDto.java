package com.mate.helgather.dto;

import com.mate.helgather.domain.Member;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberRequestDto {
    private String userName;
    private String phone;
    private String nickname;
    private String password;
    private int birthYear;
    private int birthMonth;
    private int birthDay;

    public Member toEntity() {
        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);

        return Member.builder()
                .userName(this.userName)
                .phone(this.phone)
                .nickname(this.nickname)
                .password(this.password)
                .birthDate(birthDate)
                .build();
    }
}
