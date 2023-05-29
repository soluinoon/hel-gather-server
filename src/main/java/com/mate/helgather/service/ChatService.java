package com.mate.helgather.service;

import com.mate.helgather.domain.MemberChatRoom;
import com.mate.helgather.domain.Message;
import com.mate.helgather.dto.ChatDto;
import com.mate.helgather.dto.ChatRequestDto;
import com.mate.helgather.dto.ChatRoomListResponseDto;
import com.mate.helgather.dto.MessagesResponseDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

    public Message saveMessage(ChatRequestDto chatRequestDTO, Long chatRoomId) {
        return messageRepository.save(Message.builder().chatRoom(chatRoomRepository.findById(chatRoomId)
                        .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_CHATROOM_ERROR)))
                .member(memberRepository.findById(chatRequestDTO.getUserId()).
                        orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR)))
                .description(chatRequestDTO.getMessage())
                .build());
    }

    public Message saveMessage(ChatDto chatDto, Long chatRoomId) {
        return messageRepository.save(Message.builder().chatRoom(chatRoomRepository.findById(chatRoomId)
                        .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_CHATROOM_ERROR)))
                .member(memberRepository.findById(chatDto.getUserId()).
                        orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR)))
                .description(chatDto.getMessage())
                .build());
    }

    public List<MessagesResponseDto> findMessages(Long chatRoomId, Long requestUserId) throws BaseException {
        if (!chatRoomRepository.existsById(chatRoomId)) {
            throw new BaseException(ErrorCode.NO_SUCH_CHATROOM_ERROR);
        }
        if (!memberRepository.existsById(requestUserId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
        if (!memberChatRoomRepository.existsByChatRoomIdAndMemberId(chatRoomId, requestUserId)) {
            throw new BaseException(ErrorCode.MEMBER_NOT_EXIST_IN_ROOM);
        }

        List<Message> messages = messageRepository.findAllByChatRoom_IdOrderByCreatedAt(chatRoomId);

        return messages.stream()
                .map(message -> new MessagesResponseDto(message, requestUserId))
                .collect(Collectors.toList());
    }

    public List<ChatRoomListResponseDto> findChatRoomsByMemberId(Long id) throws BaseException {
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
