package com.mate.helgather.controller;

import com.mate.helgather.dto.ChatRequestDto;
import com.mate.helgather.dto.ChatRoomListResponseDto;
import com.mate.helgather.dto.MessagesResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations template;
    private final static ConcurrentHashMap<Long, Long> prevMap = new ConcurrentHashMap<>();
    /**
     * 채팅 발행 API 명세서 6번
     * 실제 URL은 pub/chats/{id}이다.
     * 구독하고 있는 모든 사용자에게 메세지를 전달한다.
     * @param chatRoomId 채팅룸 id
     * @param chatRequestDto 채팅 DTO
     */
    @MessageMapping("/chatroom/{id}") // 실제론 메세지 매핑으로 pub/chatroom/{id} 임
    public void pubMessage(@DestinationVariable("id") Long id, ChatDto chatDTO) throws Exception {
        log.info("chat {} send by {} to room number{}", chatDTO.getMessage(), chatDTO.getUserId(), id);
        chatService.saveMessage(chatDTO, id);
        template.convertAndSend("/sub/chatroom/" + id, chatDTO);
    }

//    @SubscribeMapping("/chats/{id}")
//    public void subMessage(@DestinationVariable("id") Long chatRoomId, ChatRequestDto chatRequestDto, SimpMessageHeaderAccessor headerAccessor) {
//        System.out.println("ChatController.subMessage");
//        Map<String, Object> headers = headerAccessor.toMap();
//        for (Map.Entry<String, Object> entry : headers.entrySet()) {
//            String headerName = entry.getKey();
//            Object headerValue = entry.getValue();
//            log.info("Header - {}: {}", headerName, headerValue);
//        }
//    }

    /**
     * 5. 채팅방 메세지 API
     * @param chatRoomId
     * @param memberId
     * @return
     */
    @GetMapping("/chats/{id}")
    public ResponseEntity<BaseResponse> findMessagesByChatRoomId(@PathVariable("id") Long chatRoomId,
                                                     @RequestParam(value = "member", required = true) Long memberId) {
        List<MessagesResponseDto> messages = chatService.findMessages(chatRoomId, memberId);
        return new ResponseEntity<>(new BaseResponse(messages), HttpStatus.OK);
    }

    /**
     * 4. 채팅방 목록 API
     * 멤버의 id를 이용해 채팅방 정보 리스트를 반환한다.
     * @param memberId 멤버의 id 이다.
     * @return List<ChatRoomListResponse> 채팅방들의 원하는 정보를 리스트로 반환
     */
    @GetMapping("/chats")
    public ResponseEntity<BaseResponse> findChatRoomsByMemberIdV2(@RequestParam(value = "member") @Valid Long memberId) {
        List<ChatRoomListResponseDto> chatRoomsByMemberId = chatService.findChatRoomsByMemberId(memberId);
        return new ResponseEntity<>(new BaseResponse(chatRoomsByMemberId), HttpStatus.OK);
    }
}
