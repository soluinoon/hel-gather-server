package com.mate.helgather.domain.category;

import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Location {
    /**
     * 지역번호 저장 enum
     *
     */
    SEOUL(2L, 22L),
    INCHEON(32L, 12L),
    KANGWON(33L, 11L),
    KYUNGGI(31L, 26L);

    private final Long locationNumber; // 지역번호 저장
    private final Long maxSubLocationNumber; // 서브 지역번호 최대값. 서브 지역번호는 1부터 차례대로 채워진다.

    public static Location of(Long locationNumber, Long subLocationNumber) {
        for (Location location : Location.values()) {
            if (location.getLocationNumber().equals(locationNumber)) {
                if (location.getMaxSubLocationNumber() >= subLocationNumber) {
                    return location;
                } else {
                    throw new BaseException(ErrorCode.NO_SUCH_SUB_LOCATION_ERROR);
                }
            }
        }
        throw new BaseException(ErrorCode.NO_SUCH_LOCATION_ERROR);
    }
}
