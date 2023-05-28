package com.mate.helgather.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class RecruitmentChatRequestDto {
    @NotNull
    Long memberId;
}
