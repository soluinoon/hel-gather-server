package com.mate.helgather.repository;

import com.mate.helgather.domain.Sbd;
import com.mate.helgather.domain.status.SbdCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SbdRepository extends JpaRepository<Sbd, Long> {
    Optional<Sbd> findByMemberIdAndCategory(Long memberId, SbdCategory category);
    List<Sbd> findAllByMemberIdAndCategory(Long memberId, SbdCategory category);
    Optional<Sbd> findTopByMemberIdAndCategoryOrderByCreatedAtDesc(Long memberId, SbdCategory category);
    @Transactional
    void deleteByMemberIdAndVideoUrlAndThumbnailUrl(Long memberId, String videoUrl, String thumbnailUrl);
}
