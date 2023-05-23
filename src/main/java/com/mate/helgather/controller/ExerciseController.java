package com.mate.helgather.controller;

import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public void saveExercise(@PathVariable("id") Long userId,
                                                     @RequestPart("file") MultipartFile multipartFile) {
        exerciseService.saveExercise(multipartFile);
    }
}
