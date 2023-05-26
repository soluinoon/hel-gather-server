package com.mate.helgather.controller;

import com.mate.helgather.dto.TodayExerciseResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.TodayExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class TodayExerciseController {
    private final TodayExerciseService todayExerciseService;

    @GetMapping("/today-exercises")
    public ResponseEntity<BaseResponse> findTodayExercise(@RequestParam("member") Long memberId) throws Exception {
        List<TodayExerciseResponseDto> todayExerciseResponseDtos = todayExerciseService.find(memberId);
        return new ResponseEntity<>(new BaseResponse(todayExerciseResponseDtos), HttpStatus.OK);
    }
    @PostMapping("/today-exercises")
    public ResponseEntity<BaseResponse> saveTodayExercise(@RequestParam("member") Long memberId,
                                                          @RequestPart("file") MultipartFile multipartFile) throws Exception {
        TodayExerciseResponseDto todayExerciseResponseDto = todayExerciseService.save(memberId, multipartFile);
        return new ResponseEntity<>(new BaseResponse(todayExerciseResponseDto), HttpStatus.OK);
    }
}
