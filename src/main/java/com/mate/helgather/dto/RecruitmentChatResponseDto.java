package com.mate.helgather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecruitmentChatResponseDto {
    Long chatRoomId;
    Long recruitmentMemberId;
    Long requestMemberId;
}
