package com.mate.helgather.controller;

import com.mate.helgather.dto.ChatRoomListResponseDto;
import com.mate.helgather.exception.BaseResponse;
import com.mate.helgather.service.ChatRoomService;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    /**
     * 멤버의 id를 이용해 채팅방 정보 리스트를 반환한다.
     * 없다면 null을 반환
     * API 명세서 4번 항목
     * @param memberId 멤버의 id 이다.
     * @return List<ChatRoomListResponse> 채팅방들의 원하는 정보를 리스트로 반환
     */
    @GetMapping("/chatrooms")
    public ResponseEntity<BaseResponse> getChatRoomsByMemberIdV2(@RequestParam(value = "member") @Valid Long memberId) {
        List<ChatRoomListResponseDto> chatRoomsByMemberId = chatRoomService.getChatRoomsByMemberId(memberId);
        return new ResponseEntity<>(new BaseResponse(chatRoomsByMemberId), HttpStatus.OK);
    }
}
