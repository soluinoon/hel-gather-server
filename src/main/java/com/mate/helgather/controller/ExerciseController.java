package com.mate.helgather.controller;

import com.mate.helgather.dto.TodayExerciseRequestDto;
import com.mate.helgather.dto.TodayExerciseResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.ExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping("/exercises")
    public ResponseEntity<BaseResponse> findAllByMemberId(@RequestParam("member") Long memberId) {
        //TODO: memberId 1에서 오류 발생
        List<TodayExerciseResponseDto> todayExerciseResponseDtos = exerciseService.findAllByMemberId(memberId);
        return new ResponseEntity<>(new BaseResponse(todayExerciseResponseDtos), HttpStatus.OK);
    }

    @PostMapping("/exercises")
    public ResponseEntity<BaseResponse> save(@RequestParam("member") Long memberId,
                                                          @RequestPart("file") MultipartFile multipartFile) {
        TodayExerciseResponseDto todayExerciseResponseDto = exerciseService.save(memberId, multipartFile);
        return new ResponseEntity<>(new BaseResponse(todayExerciseResponseDto), HttpStatus.OK);
    }

    @DeleteMapping("/exercises")
    public ResponseEntity<BaseResponse> delete(@PathVariable("member") Long memberId,
                                                         TodayExerciseRequestDto todayExerciseRequestDto) throws Exception {
        exerciseService.delete(memberId, todayExerciseRequestDto);
        return new ResponseEntity<>(new BaseResponse("삭제 성공"), HttpStatus.OK);
    }
}
