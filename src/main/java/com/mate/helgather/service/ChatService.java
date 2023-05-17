package com.mate.helgather.service;

import com.mate.helgather.domain.*;
import com.mate.helgather.dto.ChatDto;
import com.mate.helgather.repository.ApplicationRepository;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;

    public void saveMessage(ChatDto chatDTO) {
        messageRepository.save(Message.builder().chatRoom(chatRoomRepository.getReferenceById(chatDTO.getRoomId()))
                .member(memberRepository.getReferenceById(chatDTO.getUserId()))
                .description(chatDTO.getMessage()).build());
    }
}
