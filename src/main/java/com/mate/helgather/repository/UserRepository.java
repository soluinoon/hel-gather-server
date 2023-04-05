package com.mate.helgather.repository;

import com.mate.helgather.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Long> {
}
