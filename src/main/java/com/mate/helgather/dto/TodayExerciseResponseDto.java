package com.mate.helgather.dto;

import lombok.Getter;

@Getter
public class TodayExerciseResponseDto {
    private String imageUrl;

    public TodayExerciseResponseDto(String imageUrl) {
        StringBuilder stringBuilder = new StringBuilder(imageUrl);
        stringBuilder.deleteCharAt(4);

        this.imageUrl = stringBuilder.toString();
    }
}
