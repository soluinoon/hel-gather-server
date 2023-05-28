package com.mate.helgather.service;

import com.mate.helgather.domain.Member;
import com.mate.helgather.domain.TodayExercise;
import com.mate.helgather.dto.TodayExerciseRequestDto;
import com.mate.helgather.dto.TodayExerciseResponseDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.AmazonS3Repository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.TodayExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final TodayExerciseRepository todayExerciseRepository;
    private final AmazonS3Repository amazonS3Repository;
    private final MemberRepository memberRepository;

    private static final String TODAY_EXERCISE_BASE_DIR = "thumbnails";

    public TodayExerciseResponseDto save(Long memberId, MultipartFile multipartFile) {
        if (!memberRepository.existsById(memberId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }

        String imageUrl = amazonS3Repository.saveV2(multipartFile, TODAY_EXERCISE_BASE_DIR);
        todayExerciseRepository.save(TodayExercise.builder()
                .member(memberRepository.getReferenceById(memberId))
                .imageUrl(imageUrl)
                .build());

        return new TodayExerciseResponseDto(imageUrl);
    }

    public List<TodayExerciseResponseDto> findAllByMemberId(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR));
        List<TodayExercise> todayExercises = todayExerciseRepository.findAllByMember(member);

        return todayExercises.stream()
                .map(todayExercise -> new TodayExerciseResponseDto(todayExercise.getImageUrl()))
                .collect(Collectors.toList());
    }

    public void delete(Long memberId, TodayExerciseRequestDto todayExerciseRequestDto) {
        if (!memberRepository.existsById(memberId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }

        amazonS3Repository.delete(extractKey(todayExerciseRequestDto.getImageUrl(), TODAY_EXERCISE_BASE_DIR));
        todayExerciseRepository.deleteByMemberIdAndImageUrl(memberId, todayExerciseRequestDto.getImageUrl());
    }

    private String extractKey(String url, String baseUrl) {
        int index = url.indexOf(baseUrl);
        return url.substring(index);
    }
}
