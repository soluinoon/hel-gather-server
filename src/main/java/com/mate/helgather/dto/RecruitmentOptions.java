package com.mate.helgather.dto;

import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
public class RecruitmentOptions {
    @NotNull(message = "로케이션은 필수입니다.")
    private Long location;
    @NotNull(message = "서브 로케이션은 필수입니다.")
    private Long subLocation;
    @Range(max = 500, message = "벤치프레스 범위는 0에서 500까지 입니다.")
    private Integer minBenchPress = 0;
    @Range(max = 500, message = "벤치프레스 범위는 0에서 500까지 입니다.")
    private Integer maxBenchPress = 500;
    @Range(max = 500, message = "스쿼트 범위는 0에서 500까지 입니다.")
    private Integer minSquat = 0;
    @Range(max = 500, message = "스쿼트 범위는 0에서 500까지 입니다.")
    private Integer maxSquat = 500;
    @Range(max = 500, message = "데드리프트 범위는 0에서 500까지 입니다.")
    private Integer minDeadLift = 0;
    @Range(max = 500, message = "데드리프트 범위는 0에서 500까지 입니다.")
    private Integer maxDeadLift = 500;

    public RecruitmentOptions(Long location, Long subLocation, Integer minBenchPress, Integer maxBenchPress, Integer minSquat, Integer maxSquat, Integer minDeadLift, Integer maxDeadLift) {
        System.out.println("location = " + location + ", subLocation = " + subLocation + ", minBenchPress = " + minBenchPress + ", maxBenchPress = " + maxBenchPress + ", minSquat = " + minSquat + ", maxSquat = " + maxSquat + ", minDeadLift = " + minDeadLift + ", maxDeadLift = " + maxDeadLift);
        this.location = location;
        this.subLocation = subLocation;
        this.minBenchPress = minBenchPress;
        this.maxBenchPress = maxBenchPress;
        this.minSquat = minSquat;
        this.maxSquat = maxSquat;
        this.minDeadLift = minDeadLift;
        this.maxDeadLift = maxDeadLift;
        validateMinAndMax();
    }

    private void validateMinAndMax() {
        if (minBenchPress > maxBenchPress || minSquat > maxSquat || minDeadLift > maxDeadLift) {
            throw new BaseException(ErrorCode.MAX_BIGGER_THAN_MIN);
        }
    }
}
