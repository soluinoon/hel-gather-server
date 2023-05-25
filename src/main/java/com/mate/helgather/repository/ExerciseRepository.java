package com.mate.helgather.repository;

import com.mate.helgather.domain.Exercise;
import com.mate.helgather.domain.status.ExerciseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByMemberIdAndCategory(Long memberId, ExerciseCategory category);
    Optional<Exercise> findTopByMemberIdAndCategoryOrderByCreatedAtDesc(Long memberId, ExerciseCategory category);
    @Transactional
    void deleteByMemberIdAndVideoUrlAndThumbnailUrl(Long memberId, String videoUrl, String thumbnailUrl);
}
