package com.mate.helgather.controller;

import com.mate.helgather.dto.TodayExerciseRequestDto;
import com.mate.helgather.dto.TodayExerciseResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.ExerciseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/exercises")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping
    public ResponseEntity<BaseResponse> findAllByMemberId(@RequestParam("member") Long memberId) {
        List<TodayExerciseResponseDto> todayExerciseResponseDtos = exerciseService.findAllByMemberId(memberId);
        return new ResponseEntity<>(new BaseResponse(todayExerciseResponseDtos), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> create(@RequestParam("member") Long memberId,
                                               @RequestPart("file") MultipartFile multipartFile) {
        TodayExerciseResponseDto todayExerciseResponseDto = exerciseService.save(memberId, multipartFile);
        return new ResponseEntity<>(new BaseResponse(todayExerciseResponseDto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> delete(@RequestParam("member") Long memberId,
                                               @RequestParam("imageUrl") String imageUrl) {
        //TODO: 임시 개편
        exerciseService.delete(memberId, new TodayExerciseRequestDto(imageUrl));
        return new ResponseEntity<>(new BaseResponse("삭제 성공"), HttpStatus.OK);
    }
}
