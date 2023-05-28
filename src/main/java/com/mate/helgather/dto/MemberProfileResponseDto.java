package com.mate.helgather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileResponseDto {

    private Long memberId;
    private String imageUrl;
    private String introduction;
    private Integer benchPress;
    private Integer squat;
    private Integer deadlift;
    private Integer exerciseCount;
}
