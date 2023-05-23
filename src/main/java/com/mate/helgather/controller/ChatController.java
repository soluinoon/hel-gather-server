package com.mate.helgather.controller;

import com.mate.helgather.dto.ChatDto;
import com.mate.helgather.dto.MessagesResponse;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
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
    public void pubMessage(@DestinationVariable("id") Long id, ChatDto chatDTO) throws Exception {
        log.info("chat {} send by {} to room number{}", chatDTO.getMessage(), chatDTO.getUserId(), id);
        chatService.saveMessage(chatDTO, id);
        template.convertAndSend("/sub/chatroom/" + id, chatDTO);
    }

    @GetMapping("/chatroom/{id}")
    public ResponseEntity<BaseResponse> getMessages(@PathVariable("id") Long chatRoomId,
                                              @RequestParam(value = "userId", required = true) Long userId) throws Exception {
        List<MessagesResponse> messages = chatService.getMessages(chatRoomId, userId);
        return new ResponseEntity<>(new BaseResponse(messages), HttpStatus.OK);
    }
}
