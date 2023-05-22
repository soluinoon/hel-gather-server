package com.mate.helgather.controller;

import com.mate.helgather.dto.ChatRoomListResponse;
import com.mate.helgather.service.ChatRoomService;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
     * @param id 멤버의 id 이다.
     * @return List<ChatRoomListResponse> 채팅방들의 원하는 정보를 리스트로 반환
     */
    @GetMapping("/members/{id}/chatrooms")
    public List<ChatRoomListResponse> getChatRoomsByMemberIdV2(@PathVariable Long id) {
        return chatRoomService.getChatRoomsByMemberId(id);
    }

}
