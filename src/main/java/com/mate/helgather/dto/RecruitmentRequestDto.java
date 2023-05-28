package com.mate.helgather.dto;

import com.mate.helgather.domain.Member;
import com.mate.helgather.domain.Recruitment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class RecruitmentRequestDto {
    @NotNull
    Long memberId;
    @NotNull
    Long location;
    @NotNull
    Long subLocation;
    @NotNull
    String title;
    @NotNull
    String description;

    public Recruitment toEntity(Member member) {
        return Recruitment.builder()
                .member(member)
                .location(this.location)
                .subLocation(this.subLocation)
                .title(this.title)
                .description(this.description)
                .build();
    }
}
