package com.mate.helgather.repository;

import com.mate.helgather.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    List<Recruitment> findAllByLocationAndSubLocationOrderByCreatedAtDesc(Long location, Long subLocation);
}
