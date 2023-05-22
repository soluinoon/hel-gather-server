package com.mate.helgather.service;

import com.mate.helgather.domain.Member;
import com.mate.helgather.dto.MemberRequestDto;
import com.mate.helgather.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원 가입 로직
    public Long createMember(MemberRequestDto memberRequestDto) {
        Member member = memberRequestDto.toEntity();  // DTO에서 Entity로 변환
        memberRepository.save(member);  // 회원 정보 저장
        return member.getId();  // 저장된 회원의 ID 반환
    }
}
