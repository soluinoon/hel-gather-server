package com.mate.helgather.controller;

import com.mate.helgather.dto.MemberResponseDto;
import com.mate.helgather.dto.MemberRequestDto;
import com.mate.helgather.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원가입 로직
    @Transactional
    @PostMapping("/members")
    public ResponseEntity<MemberResponseDto> createMember(@RequestBody MemberRequestDto memberRequestDto) {
        Long memberId = memberService.createMember(memberRequestDto);
        return ResponseEntity.ok(new MemberResponseDto(memberId));
    }
}
