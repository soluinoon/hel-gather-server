package com.mate.helgather.controller;

import com.mate.helgather.domain.Message;
import com.mate.helgather.dto.ChatRequestDto;
import com.mate.helgather.dto.ChatResponseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations template;

    @MessageMapping("/chatroom/{id}") // 실제론 메세지 매핑으로 pub/chatroom/{id} 임
    public void pubMessage(@DestinationVariable("id") Long chatRoomId, ChatRequestDto chatRequestDTO) throws Exception {
        log.info("chat {} send by {} to room number{}", chatRequestDTO.getMessage(), chatRequestDTO.getUserId(), chatRoomId);
        Message message = chatService.saveMessage(chatRequestDTO, chatRoomId);
        template.convertAndSend("/sub/chatroom/" + chatRoomId, new ChatResponseDto(chatRequestDTO, message));
    }

    @GetMapping("/chatroom/{id}")
    public ResponseEntity<BaseResponse> getMessages(@PathVariable("id") Long chatRoomId,
                                              @RequestParam(value = "userId", required = true) Long userId) throws Exception {
        List<MessagesResponse> messages = chatService.getMessages(chatRoomId, userId);
        return new ResponseEntity<>(new BaseResponse(messages), HttpStatus.OK);
    }
}
