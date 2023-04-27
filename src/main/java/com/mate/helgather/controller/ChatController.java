package com.mate.helgather.controller;

import com.mate.helgather.domain.Member;
import com.mate.helgather.dto.ChatDTO;
import com.mate.helgather.dto.MemberDTO;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
/*
v1 특징
- 1:1 대화만
- 한사람은 하나의 채팅방만 가질 수 있음.
*/
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations template;

    @MessageMapping("/chat/send") // 실제론 메세지 매핑으로 pub/chat/send 임
    public void sendMessage(@Payload ChatDTO chatDTO) {
        log.info("chat {} send by {} to room number{}", chatDTO.getMessage(), chatDTO.getSender(), chatDTO.getRoomId());
        template.convertAndSend("/sub/chat/room" + chatDTO.getRoomId(), chatDTO);
    }
}
