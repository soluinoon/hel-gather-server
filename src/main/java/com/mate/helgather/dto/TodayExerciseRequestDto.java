package com.mate.helgather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class TodayExerciseRequestDto {
    private String imageUrl;
}
