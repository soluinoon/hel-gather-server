package com.mate.helgather.repository;

import com.mate.helgather.domain.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
    boolean existsByMember_id(Long memberId);

    Optional<MemberProfile> findByMember_id(Long memberId);
}
