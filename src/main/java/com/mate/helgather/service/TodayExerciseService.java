package com.mate.helgather.service;

import com.mate.helgather.domain.TodayExercise;
import com.mate.helgather.dto.TodayExerciseResponseDto;
import com.mate.helgather.repository.AmazonS3Repository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.TodayExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TodayExerciseService {
    private final TodayExerciseRepository todayExerciseRepository;
    private final AmazonS3Repository amazonS3Repository;
    private final MemberRepository memberRepository;

    private static final String TODAY_EXERCISE_BASE_DIR = "thumbnails";

    public TodayExerciseResponseDto save(Long memberId, MultipartFile multipartFile) throws Exception {
        String imageUrl = amazonS3Repository.saveV2(multipartFile, TODAY_EXERCISE_BASE_DIR);
        todayExerciseRepository.save(TodayExercise.builder()
                        .member(memberRepository.getReferenceById(memberId))
                        .imageUrl(imageUrl)
                        .build());
        return new TodayExerciseResponseDto(imageUrl);
    }
}
