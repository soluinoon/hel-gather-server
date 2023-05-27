package com.mate.helgather.controller;

import com.mate.helgather.domain.status.SbdCategory;
import com.mate.helgather.dto.SbdRequestDto;
import com.mate.helgather.dto.SbdResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.SbdService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
public class SbdController {
    private final SbdService sbdService;

    /**
     * SBD 인증 작성 API
     * API 명세서 12번
     * @param memberId 멤버 id
     * @param multipartFile 동영상 파일
     * @param category 운동 카테고리이다. 스쿼트, 데드리프트, 벤치프레스로 나뉨.
     */
    @PostMapping(value = "/sbd", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> saveSBD(@PathVariable("member") Long memberId,
                                                @RequestParam("category") String category,
                                                @RequestPart("file") MultipartFile multipartFile) throws Exception {
        SbdResponseDto sbdResponseDto = sbdService.saveSBD(SbdCategory.of(category), memberId, multipartFile);
        return new ResponseEntity<>(new BaseResponse(sbdResponseDto), HttpStatus.OK);
    }
    /**
     * SBD 운동 인증 조회 API
     * @param memberId 멤버의 id다
     * @return SBD를 각각 최신 하나씩 돌려줌
     */
    @GetMapping("/sbd")
    public ResponseEntity<BaseResponse> findSBD(@RequestParam("member") Long memberId) throws Exception {
        List<SbdResponseDto> sbdResponseDtoList = sbdService.findSBD(memberId);
        return new ResponseEntity<>(new BaseResponse(sbdResponseDtoList), HttpStatus.OK);
    }

    /**
     * 운동 인증 삭제 API
     * 클라이언트에서 url을 받아와서 삭제시킨다.
     * API 명세서 14번
     * @param memberId 유저 id
     * @param sbdRequestDto 비디오, 썸네일 url을 담고있다.
     */
    @DeleteMapping("/sbd")
    public ResponseEntity<BaseResponse> deleteSBDV1(@PathVariable("member") Long memberId,
                                                    SbdRequestDto sbdRequestDto) throws Exception {
        sbdService.deleteExercise(memberId, sbdRequestDto);
        return new ResponseEntity<>(new BaseResponse("삭제 성공"), HttpStatus.OK);
    }
}
