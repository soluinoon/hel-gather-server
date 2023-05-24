package com.mate.helgather.controller;

import com.mate.helgather.dto.ExerciseResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;

    /**
     * 운동 게시물 작성 API
     *
     */
    @PostMapping(value = "/members/{id}/exercises", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> saveExercise(@PathVariable("id") Long userId,
                                                     @RequestPart("file") MultipartFile multipartFile) throws Exception {
        ExerciseResponseDto exerciseResponseDto = exerciseService.saveExercise(userId, multipartFile);
        return new ResponseEntity<>(new BaseResponse(exerciseResponseDto), HttpStatus.OK);
    }
    /**
     * 운동 게시물 조회 API
     *
     */
    @GetMapping("/members/{id}/exercises")
    public ResponseEntity<BaseResponse> findExercise(@PathVariable("id") Long userId) throws Exception {
        List<ExerciseResponseDto> exerciseResponseDtoList = exerciseService.findExercisesById(userId);
        return new ResponseEntity<>(new BaseResponse(exerciseResponseDtoList), HttpStatus.OK);
    }
}
