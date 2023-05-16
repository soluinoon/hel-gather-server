package com.mate.helgather.repository;

import com.mate.helgather.domain.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<Application, Long> {
}
