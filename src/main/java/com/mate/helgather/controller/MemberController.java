package com.mate.helgather.controller;

import com.mate.helgather.domain.status.SbdCategory;
import com.mate.helgather.dto.*;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    //회원가입 로직
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> createMember(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto result = memberService.createMember(memberRequestDto);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

//    //로그인 로직
//    @PostMapping("/login")
//    public ResponseEntity<BaseResponse> loginMember(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
//        String nickname = memberLoginRequestDto.getNickname();
//        String password = memberLoginRequestDto.getPassword();
//
//        MemberLoginResponseDto memberLoginResponseDto = memberService.loginMember(nickname, password);
//        return new ResponseEntity<>(new BaseResponse(memberLoginResponseDto), HttpStatus.OK);
//    }

    //프로필 입력 정보 로직(이미지 제외)
    @PostMapping("/profile/{memberId}")
    public ResponseEntity<BaseResponse> createProfile(@PathVariable Long memberId,
                                                      @RequestBody MemberProfileRequestDto memberProfileRequestDto) {
        String introduction = memberProfileRequestDto.getIntroduction();
        Integer benchPress = memberProfileRequestDto.getBenchPress();
        Integer squat = memberProfileRequestDto.getSquat();
        Integer deadlift = memberProfileRequestDto.getDeadlift();

        MemberProfileResponseDto memberProfileResponseDto = memberService.createProfile(memberId, introduction, benchPress, squat, deadlift);
        return new ResponseEntity<>(new BaseResponse(memberProfileResponseDto), HttpStatus.OK);
    }

    //프로필 이미지 정보 입력 로직
    @PostMapping("/profile/{memberId}/image")
    public ResponseEntity<BaseResponse> createProfileImage(@PathVariable Long memberId,
                                                           @RequestPart("file") MultipartFile multipartFile) {
        MemberProfileImageResponseDto dto = memberService.createProfileImage(memberId, multipartFile);
        return new ResponseEntity<>(new BaseResponse(dto), HttpStatus.OK);
    }
}
