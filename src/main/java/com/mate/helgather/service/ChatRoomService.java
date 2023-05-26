package com.mate.helgather.service;

import com.mate.helgather.domain.MemberChatRoom;
import com.mate.helgather.domain.Message;
import com.mate.helgather.dto.ChatRoomListResponseDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatRoomService {
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

    public List<ChatRoomListResponseDto> getChatRoomsByMemberId(Long id) throws BaseException {
        if (!memberRepository.existsById(id)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
        List<MemberChatRoom> memberChatRooms = memberChatRoomRepository.findAllByMemberId(id);
        List<ChatRoomListResponseDto> chatRoomListResponsDtos = new ArrayList<>();

        for (MemberChatRoom memberChatRoom : memberChatRooms) {
            Message recentMessage = messageRepository.findTopByChatRoomOrderByCreatedAtDesc(memberChatRoom.getChatRoom())
                    .orElse(Message.builder()
                            .chatRoom(memberChatRoom.getChatRoom())
                            .description("아직 채팅이 없습니다.")
                            .member(null)
                            .createdAt(LocalDateTime.now())
                            .build());
            chatRoomListResponsDtos.add(new ChatRoomListResponseDto(memberChatRoom.getMember().getNickname(),
                    recentMessage.getCreatedAt().toString(),
                    recentMessage.getDescription(),
                    memberChatRoom.getChatRoom().getId()));
        }
        return chatRoomListResponsDtos;
    }
}
