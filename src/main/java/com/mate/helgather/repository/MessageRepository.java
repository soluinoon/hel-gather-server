package com.mate.helgather.repository;

import com.mate.helgather.domain.ChatRoom;
import com.mate.helgather.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findTopByChatRoomOrderByCreatedDateDesc(ChatRoom chatRoom);
}
