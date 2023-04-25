package com.mate.helgather.controller;

import com.mate.helgather.domain.Member;
import com.mate.helgather.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
@Slf4j
/*
v1 특징
- 1:1 대화만
- 한사람은 하나의 채팅방만 가질 수 있음.
 */
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public void getChatRoom(Member member) {
        chatService.getChatRoom(member);
    }
}
