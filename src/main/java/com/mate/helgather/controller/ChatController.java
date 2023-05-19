package com.mate.helgather.controller;

import com.mate.helgather.dto.ChatDto;
import com.mate.helgather.dto.MessagesResponse;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations template;

    @GetMapping("/chat/test")
    public void testChat() {
        chatService.testChat();
    }
    @MessageMapping("/chatroom/{id}") // 실제론 메세지 매핑으로 pub/chatroom/{id} 임
    public void sendMessage(@DestinationVariable("id") Long id, ChatDto chatDTO) {
        log.info("chat {} send by {} to room number{}", chatDTO.getMessage(), chatDTO.getUserId(), chatDTO.getRoomId());
        chatService.saveMessage(chatDTO);
        template.convertAndSend("/sub/chatroom/" + chatDTO.getRoomId(), chatDTO);
    }

    @GetMapping("/chatroom/{id}")
    public List<MessagesResponse> getMessages(@PathVariable("id") Long id) {
        return chatService.getMessages(id);
    }
}
