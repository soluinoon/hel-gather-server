package com.mate.helgather.service;

import com.mate.helgather.domain.Member;
import com.mate.helgather.domain.Recruitment;
import com.mate.helgather.domain.category.Location;
import com.mate.helgather.dto.RecruitmentListResponseDto;
import com.mate.helgather.dto.RecruitmentRequestDto;
import com.mate.helgather.dto.RecruitmentResponseDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(RecruitmentRequestDto recruitmentRequestDto) {
        Member member = memberRepository.findById(recruitmentRequestDto.getMemberId())
                .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR));

        Recruitment recruitment = recruitmentRequestDto.toEntity(member);

        Location location = Location.of(recruitment.getLocation(), recruitment.getSubLocation());

        recruitmentRepository.save(recruitment);
    }

    @Transactional
    public List<RecruitmentListResponseDto> findAll(Long locationNumber, Long subLocationNumber) {
        Location location = Location.of(locationNumber, subLocationNumber);

        // 날짜순으로 내림차순으로 정렬되서 줌
        // TODO: 만약, 멤버가 없어졌다면(탈퇴) 어떻게 되는지 알아보기.
        List<Recruitment> recruitments = recruitmentRepository.findAllByLocationAndSubLocationOrderByCreatedAtDesc(locationNumber, subLocationNumber);

        return recruitments.stream().map(RecruitmentListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecruitmentResponseDto findById(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_RECRUITMENT_ERROR));
        return new RecruitmentResponseDto(recruitment);
    }
}
