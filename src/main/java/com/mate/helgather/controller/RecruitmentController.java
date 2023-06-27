package com.mate.helgather.controller;

import com.mate.helgather.dto.*;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<BaseResponse> create(@RequestBody RecruitmentRequestDto recruitmentRequestDto) {
        recruitmentService.saveV2(recruitmentRequestDto);
        return new ResponseEntity<>(new BaseResponse("성공"), HttpStatus.OK);
    }

    /**
     * 42. 모집글 목록 조회 API
     * @param location
     * @param subLocation
     * @return
     */
//    @GetMapping
//    public ResponseEntity<BaseResponse> findAllByCategory(@RequestParam Long location, @RequestParam Long subLocation) {
//        List<RecruitmentListResponseDto> recruitmentListResponseDtos = recruitmentService.findAllByCategory(location, subLocation);
//
//        return new ResponseEntity<>(new BaseResponse(recruitmentListResponseDtos), HttpStatus.OK);
//    }

    /**
     *
     * @return
     */
    @GetMapping("/all")
    public ResponseEntity<BaseResponse> findAll() {
        List<RecruitmentListResponseDto> recruitmentListResponseDtos = recruitmentService.findAll();

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

    /**
     * 45. 모집글 채팅신청 API
     * 핵심 로직
     * 채팅신청버튼 클릭 -> 채팅방 만들고 n:m 테이블에 저장 -> 클라이언트에게 채팅방 id와 유저2명 id 반환
     * @param id 모집글 id
     * @param recruitmentChatRequestDto 클라이언트에게 채팅방 id와 유저2명 id 반환
     * @return
     */
//    @PostMapping("/{id}/chat")
//    public ResponseEntity<BaseResponse> createChatRoom(@PathVariable Long id, @RequestBody RecruitmentChatRequestDto recruitmentChatRequestDto) {
//        RecruitmentChatResponseDto recruitmentChatResponseDto = recruitmentService.createChatRoom(id, recruitmentChatRequestDto.getMemberId());
//        return new ResponseEntity<>(new BaseResponse(recruitmentChatResponseDto), HttpStatus.OK);
//    }

    /**
     * 45. 모집글 채팅신청 API
     * 핵심 로직
     * 채팅신청버튼 클릭 -> 채팅방 만들고 n:m 테이블에 저장 -> 클라이언트에게 채팅방 id와 유저2명 id 반환
     * @param id 모집글 id
     * @param recruitmentChatRequestDto 클라이언트에게 채팅방 id와 유저2명 id 반환
     * @return
     */
    @PostMapping("/{id}/chat")
    public ResponseEntity<BaseResponse> apply(@PathVariable Long id, @RequestBody RecruitmentChatRequestDto recruitmentChatRequestDto) {
        RecruitmentChatResponseDto recruitmentChatResponseDto = recruitmentService.apply(id, recruitmentChatRequestDto.getMemberId());
        return new ResponseEntity<>(new BaseResponse(recruitmentChatResponseDto), HttpStatus.OK);
    }

    /**
     * 모집글 조회 API
     * 다양한 옵션들로 찾을 수 있다.
     * 조건들을 모두 담아서 보내줘야함.
     * @param recruitmentOptions
     * @param bindingResult
     * @// TODO: 2023/06/16 조건이 일부 없어도 동작되게 만들고 싶은데 방법을 찾아볼 것.
     * @return 베이스 리스폰스
     */
    @GetMapping
    public ResponseEntity<BaseResponse> findByOptions(@Valid RecruitmentOptions recruitmentOptions, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BaseException(ErrorCode.FORMAT_ERROR);
        }
        //TODO: 조건이 일부 없어도 동작되게 만들고 싶은데 방법 찾아볼 것.
        List<RecruitmentListResponseDto> recruitmentListResponseDtos = recruitmentService.findByOptions(recruitmentOptions);
        return new ResponseEntity<>(new BaseResponse(recruitmentListResponseDtos), HttpStatus.OK);
    }
}
