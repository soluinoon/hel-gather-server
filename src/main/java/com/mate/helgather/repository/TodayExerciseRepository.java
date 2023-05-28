package com.mate.helgather.repository;

import com.mate.helgather.domain.Member;
import com.mate.helgather.domain.TodayExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TodayExerciseRepository extends JpaRepository<TodayExercise, Long> {
    List<TodayExercise> findAllByMember(Member member);
    @Transactional
    void deleteByMemberIdAndImageUrl(Long memberId, String imageUrl);
}
