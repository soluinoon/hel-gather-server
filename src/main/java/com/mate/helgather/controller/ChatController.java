package com.mate.helgather.controller;

import com.mate.helgather.dto.ChatDto;
import com.mate.helgather.dto.ChatRoomListResponseDto;
import com.mate.helgather.dto.MessagesResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
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

     */
    @MessageMapping("/chatroom/{id}") // 실제론 메세지 매핑으로 pub/chatroom/{id} 임
    public void pubMessage(@DestinationVariable("id") Long id, ChatDto chatDTO) {
        log.info("chat {} send by {} to room number{}", chatDTO.getMessage(), chatDTO.getUserId(), id);
        chatService.saveMessage(chatDTO, id);
        template.convertAndSend("/sub/chatroom/" + id, chatDTO);
    }

    /**
     * 5. 채팅방 메세지 API
     * @param chatRoomId
     * @param memberId
     * @return
     */
    @GetMapping("/chats/{id}")
    public ResponseEntity<BaseResponse> findMessagesByChatRoomId(@PathVariable("id") Long chatRoomId,
                                                                 @RequestParam(value = "member") Long memberId,
                                                                 @PageableDefault Pageable pageable) {
        log.info("size = {}, page = {}, sorted = {}", pageable.getPageSize(), pageable.getPageNumber(), pageable.getSort());
        List<MessagesResponseDto> messages = chatService.findMessages(chatRoomId, memberId, pageable);
        return new ResponseEntity<>(new BaseResponse(messages), HttpStatus.OK);
    }

    /**
     * 31. 채팅방 목록 API
     * 멤버의 id를 이용해 채팅방 정보 리스트를 반환한다.
     * @param memberId 멤버의 id 이다.
     * @return List<ChatRoomListResponse> 채팅방들의 원하는 정보를 리스트로 반환
     */
//    @GetMapping("/chats")
//    public ResponseEntity<BaseResponse> findChatRoomsByMemberIdV2(@RequestParam(value = "member") @Valid Long memberId) {
//        List<ChatRoomListResponseDto> chatRoomsByMemberId = chatService.findChatRoomsByMemberId(memberId);
//        return new ResponseEntity<>(new BaseResponse(chatRoomsByMemberId), HttpStatus.OK);
//    }

    /**
     * 31. 채팅방 목록 API
     * 멤버의 id를 이용해 채팅방 정보 리스트를 반환한다.
     * 발표 전날 만든 메서드로, 기본이미지, 채팅방 이름은 모집글 제목으로
     * 단체 채팅방으로 가정한다.
     * @param memberId 멤버의 id 이다.
     * @return List<ChatRoomListResponse> 채팅방들의 원하는 정보를 리스트로 반환
     */
    @GetMapping("/chats")
    public ResponseEntity<BaseResponse> findChatRoomsByMemberIdV3(@RequestParam(value = "member") @Valid Long memberId) {
        List<ChatRoomListResponseDto> chatRoomsByMemberId = chatService.findChatRoomsByMemberIdV2(memberId);
        return new ResponseEntity<>(new BaseResponse(chatRoomsByMemberId), HttpStatus.OK);
    }
}
