package com.mate.helgather.dto;

import com.mate.helgather.domain.Recruitment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class RecruitmentResponseDto {
    Long recruitmentId;
    Long memberId;
    String nickname;
    String title;
    String description;
    LocalDateTime createdAt;

    public RecruitmentResponseDto(Recruitment recruitment) {
        this.recruitmentId = recruitment.getId();
        this.memberId = recruitment.getMember().getId();
        this.nickname = recruitment.getMember().getNickname();
        this.title = recruitment.getTitle();
        this.description = recruitment.getDescription();
        this.createdAt = recruitment.getCreatedAt();
    }
}
