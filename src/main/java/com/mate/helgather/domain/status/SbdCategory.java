package com.mate.helgather.domain.status;

import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum SbdCategory {
    DEAD_LIFT("dead_lift"),
    SQUAT("squat"),
    BENCH_PRESS("bench_press"),
    TODAY("today");

    private String category;

    SbdCategory(String category) {
        this.category = category;
    }

    public static SbdCategory of(String category) throws RuntimeException {
        for (SbdCategory sbdCategory : SbdCategory.values()) {
            if (sbdCategory.getCategory().equals(category)) {
                return sbdCategory;
            }
        }
        throw new BaseException(ErrorCode.NO_SUCH_CATEGORY_ERROR);
    }
}
