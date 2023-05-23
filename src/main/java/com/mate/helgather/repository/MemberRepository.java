package com.mate.helgather.repository;

import com.mate.helgather.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsById(Long id);

    boolean existsByPhone(String phone);

    boolean existsByNickname(String nickname);
}
