package com.mate.helgather.dto;

import com.mate.helgather.domain.Exercise;
import com.mate.helgather.domain.status.ExerciseCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExerciseResponseDto {
    ExerciseCategory category;
    String videoUrl;
    String thumbNailUrl;

    public ExerciseResponseDto(Exercise exercise) {
        this.category = exercise.getCategory();
        this.videoUrl = exercise.getVideoUrl();
        this.thumbNailUrl = exercise.getThumbnailUrl();
    }
}
