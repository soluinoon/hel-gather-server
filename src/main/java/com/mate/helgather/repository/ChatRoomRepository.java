package com.mate.helgather.repository;

import com.mate.helgather.domain.ChatRoom;
import com.mate.helgather.domain.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRecruitment(Recruitment recruitment);
}
