package com.mate.helgather.controller;

import com.mate.helgather.dto.TodayExerciseResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.TodayExerciseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class TodayExerciseController {
    private final TodayExerciseService todayExerciseService;

    @PostMapping("/today-exercise")
    public ResponseEntity<BaseResponse> saveSbd(@RequestParam("memberId") Long memberId,
                                                @RequestPart("file") MultipartFile multipartFile) throws Exception {
        TodayExerciseResponseDto todayExerciseResponseDto = todayExerciseService.save(memberId, multipartFile);
        return new ResponseEntity<>(new BaseResponse(todayExerciseResponseDto), HttpStatus.OK);
    }
}
