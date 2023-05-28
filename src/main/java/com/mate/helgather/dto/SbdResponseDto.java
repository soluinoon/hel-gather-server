package com.mate.helgather.dto;

import com.mate.helgather.domain.Sbd;
import com.mate.helgather.domain.status.SbdCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SbdResponseDto {
    SbdCategory category;
    String videoUrl;
    String thumbNailUrl;

    public SbdResponseDto(Sbd exercise) {
        StringBuilder stringBuilder;

        this.category = exercise.getCategory();
        if (exercise.getVideoUrl().equals("")) {
            this.videoUrl = exercise.getVideoUrl();
        } else {
            stringBuilder = new StringBuilder(exercise.getVideoUrl());
            stringBuilder.deleteCharAt(4);
            this.videoUrl = stringBuilder.toString();
        }
        if (exercise.getVideoUrl().equals("")) {
            this.thumbNailUrl = exercise.getThumbnailUrl();
        } else {
            stringBuilder = new StringBuilder(exercise.getThumbnailUrl());
            stringBuilder.deleteCharAt(4);
            this.thumbNailUrl = stringBuilder.toString();
        }
    }
}
