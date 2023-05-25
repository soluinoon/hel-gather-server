package com.mate.helgather.domain.status;

import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ExerciseCategory {
    DEAD_LIFT("dead_lift"),
    SQUAT("squat"),
    BENCH_PRESS("bench_press"),
    TODAY("today");

    private String category;

    ExerciseCategory(String category) {
        this.category = category;
    }

    public static ExerciseCategory of(String category) throws Exception {
        for (ExerciseCategory exerciseCategory : ExerciseCategory.values()) {
            if (exerciseCategory.getCategory().equals(category)) {
                return exerciseCategory;
            }
        }
        throw new BaseException(ErrorCode.NO_SUCH_CATEGORY_ERROR);
    }
}
