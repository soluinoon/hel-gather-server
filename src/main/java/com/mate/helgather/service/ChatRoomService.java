package com.mate.helgather.service;

import com.mate.helgather.domain.MemberChatRoom;
import com.mate.helgather.domain.Message;
import com.mate.helgather.dto.ChatRoomListResponse;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberChatRoomRepository;
import com.mate.helgather.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

    public List<ChatRoomListResponse> getChatRoomsByMemberId(Long id) {
        List<MemberChatRoom> memberChatRooms = memberChatRoomRepository.findAllByMemberId(id);
        List<ChatRoomListResponse> chatRoomListResponses = new ArrayList<>();

        for (MemberChatRoom memberChatRoom : memberChatRooms) {
            Message recentMessage = messageRepository.findTopByChatRoomOrderByCreatedDateDesc(memberChatRoom.getChatRoom())
                    .orElse(null);
            chatRoomListResponses.add(new ChatRoomListResponse(memberChatRoom.getMember().getNickname(),
                    recentMessage.getCreatedDate().toString(),
                    recentMessage.getDescription(),
                    memberChatRoom.getId()));
        }
        return chatRoomListResponses;
    }
}
