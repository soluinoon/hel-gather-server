package com.mate.helgather.repository;

import com.mate.helgather.domain.Recruitment;
import com.mate.helgather.dto.RecruitmentOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    List<Recruitment> findAllByLocationAndSubLocationOrderByCreatedAtDesc(Long location, Long subLocation);
    // SBD범위와 지역번호로 찾음.
    @Query("select r from Recruitment r inner join r.member m inner join m.memberProfile mp " +
            "where mp.benchPress between :#{#recruitmentOptions.minBenchPress} and :#{#recruitmentOptions.maxBenchPress} " +
            "and mp.deadLift between :#{#recruitmentOptions.minDeadLift} and :#{#recruitmentOptions.maxDeadLift} " +
            "and mp.squat between :#{#recruitmentOptions.minSquat} and :#{#recruitmentOptions.maxSquat} " +
            "and r.location = :#{#recruitmentOptions.location} and r.subLocation = :#{#recruitmentOptions.subLocation} " +
            "order by r.createdAt desc")
    List<Recruitment> findAllByRecruitmentOptions(@Param("recruitmentOptions") RecruitmentOptions recruitmentOptions);
}
