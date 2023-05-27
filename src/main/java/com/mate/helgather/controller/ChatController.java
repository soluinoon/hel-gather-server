package com.mate.helgather.controller;

import com.mate.helgather.domain.Message;
import com.mate.helgather.dto.ChatRequestDto;
import com.mate.helgather.dto.ChatResponseDto;
import com.mate.helgather.dto.MessagesResponseDto;
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

    /**
     * 채팅 발행 API
     * 실제 URL은 pub/chatroom/{id}이다.
     * API 명세서 6번
     * 구독하고 있는 모든 사용자에게 메세지를 전달한다.
     * @param chatRoomId 채팅룸 id
     * @param chatRequestDto 채팅 DTO
     */
    @MessageMapping("/chatrooms/{id}") // 실제론 메세지 매핑으로 pub/chatroom/{id} 임
    public void pubMessage(@DestinationVariable("id") Long chatRoomId, ChatRequestDto chatRequestDto) {
        log.info("chat {} send by {} to room number{}", chatRequestDto.getMessage(), chatRequestDto.getUserId(), chatRoomId);
        Message message = chatService.saveMessage(chatRequestDto, chatRoomId);
        template.convertAndSend("/sub/chatrooms/" + chatRoomId, new ChatResponseDto(chatRequestDto, message));
    }

    @GetMapping("/chatrooms/{id}")
    public ResponseEntity<BaseResponse> findMessages(@PathVariable("id") Long chatRoomId,
                                                     @RequestParam(value = "member", required = true) Long memberId) {
        List<MessagesResponseDto> messages = chatService.findMessages(chatRoomId, memberId);
        return new ResponseEntity<>(new BaseResponse(messages), HttpStatus.OK);
    }
}
