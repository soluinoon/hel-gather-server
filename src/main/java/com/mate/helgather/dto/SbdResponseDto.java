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
        this.category = exercise.getCategory();
        this.videoUrl = exercise.getVideoUrl();
        this.thumbNailUrl = exercise.getThumbnailUrl();
    }
}
