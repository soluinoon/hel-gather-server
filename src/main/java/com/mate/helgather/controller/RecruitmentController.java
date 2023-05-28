package com.mate.helgather.controller;

import com.mate.helgather.dto.RecruitmentRequestDto;
import com.mate.helgather.dto.RecruitmentListResponseDto;
import com.mate.helgather.dto.RecruitmentResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/recruitments")
public class RecruitmentController {
    private final RecruitmentService recruitmentService;

    /**
     * 41. 모집글 생성 API
     * @param recruitmentRequestDto
     * @return
     */
    @PostMapping()
    public ResponseEntity<BaseResponse> save(@Valid RecruitmentRequestDto recruitmentRequestDto) {
        recruitmentService.save(recruitmentRequestDto);

        return new ResponseEntity<>(new BaseResponse("성공"), HttpStatus.OK);
    }

    /**
     * 42. 모집글 목록 조회 API
     * @param location
     * @param subLocation
     * @return
     */
    @GetMapping
    public ResponseEntity<BaseResponse> findAll(@RequestParam Long location, @RequestParam Long subLocation) {
        List<RecruitmentListResponseDto> recruitmentListResponseDtos = recruitmentService.findAll(location, subLocation);

        return new ResponseEntity<>(new BaseResponse(recruitmentListResponseDtos), HttpStatus.OK);
    }

    /**
     * 43. 모집글 상세 정보 API
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) {
        RecruitmentResponseDto recruitmentResponseDto = recruitmentService.findById(id);

        return new ResponseEntity<>(new BaseResponse(recruitmentResponseDto), HttpStatus.OK);
    }
}
