package com.mate.helgather.dto;

import com.mate.helgather.domain.Exercise;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExerciseResponseDto {
    String videoUrl;
    String thumbNailUrl;

    public ExerciseResponseDto(Exercise exercise) {
        this.videoUrl = exercise.getVideoUrl();
        this.thumbNailUrl = exercise.getThumbnailUrl();
    }
}
