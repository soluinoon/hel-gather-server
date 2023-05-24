package com.mate.helgather.repository;

import com.mate.helgather.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsById(Long id);

    boolean existsByPhone(String phone);

    boolean existsByNickname(String nickname);

    Optional<Member> findByNickname(String nickname);
}
