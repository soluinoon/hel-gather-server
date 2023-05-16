package com.mate.helgather.controller;

import com.mate.helgather.dto.ChatDto;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations template;

    @MessageMapping("/chatroom/{chatroomId}") // 실제론 메세지 매핑으로 pub/chatroom/{id} 임
    public void sendMessage(ChatDto chatDTO, @PathVariable Long chatroomId) {
        log.info("chat {} send by {} to room number{}", chatDTO.getMessage(), chatDTO.getUserId(), chatroomId);
        chatService.saveMessage(chatDTO);
        template.convertAndSend("/sub/chatroom/" + chatroomId, chatDTO);
    }
}
