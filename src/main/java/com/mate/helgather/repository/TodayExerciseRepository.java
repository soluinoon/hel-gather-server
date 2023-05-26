package com.mate.helgather.repository;

import com.mate.helgather.domain.TodayExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodayExerciseRepository extends JpaRepository<TodayExercise, Long> {
    List<TodayExercise> findAllByMemberId(Long memberId);
}
