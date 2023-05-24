package com.mate.helgather.controller;

import com.mate.helgather.dto.MemberLoginRequestDto;
import com.mate.helgather.dto.MemberResponseDto;
import com.mate.helgather.dto.MemberRequestDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    //회원가입 로직
    @Transactional
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> createMember(@RequestBody MemberRequestDto memberRequestDto) throws BaseException {
        MemberResponseDto result = memberService.createMember(memberRequestDto);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    //로그인 로직
    @Transactional
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> loginMember(@RequestBody MemberLoginRequestDto memberLoginRequestDto) throws BaseException {
        MemberResponseDto result = memberService.loginMember(memberLoginRequestDto);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }
}
