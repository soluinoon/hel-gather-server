package com.mate.helgather.controller;

import com.mate.helgather.dto.ChatRoomDTO;
import com.mate.helgather.dto.MemberDTO;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {
    private final ChatService chatService;

    /**
     * 멤버의 id를 이용해 채팅방을 반환한다.
     * 없다면 null을 반환
     * @param id 멤버의 id 이다.
     * @return ChatRoomDTO 채팅방의 id가 담겨있다.
     */
    @GetMapping("/members/{id}/chatroom")
    public ChatRoomDTO getChatRoomByMemberId(@PathVariable Long id) {
        return chatService.getChatRoomByMemberId(id);
    }
}
