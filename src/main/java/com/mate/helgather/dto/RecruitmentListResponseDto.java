package com.mate.helgather.dto;

import com.mate.helgather.domain.Recruitment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class RecruitmentListResponseDto {
    Long recruitmentId;
    String title;
    String nickname;
    LocalDateTime createdAt;

    public RecruitmentListResponseDto(Recruitment recruitment) {
        this.recruitmentId = recruitment.getId();
        this.title = recruitment.getTitle();
        this.nickname = recruitment.getMember().getNickname();
        this.createdAt = recruitment.getCreatedAt();
    }
}
