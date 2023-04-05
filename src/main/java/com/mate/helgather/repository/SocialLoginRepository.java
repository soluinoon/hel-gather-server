package com.mate.helgather.repository;

import com.mate.helgather.domain.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
}
