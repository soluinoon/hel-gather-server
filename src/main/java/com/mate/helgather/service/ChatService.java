package com.mate.helgather.service;

import com.mate.helgather.domain.Message;
import com.mate.helgather.dto.ChatRequestDto;
import com.mate.helgather.dto.MessagesResponseDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

    public Message saveMessage(ChatRequestDto chatRequestDTO, Long chatRoomId) {
        return messageRepository.save(Message.builder().chatRoom(chatRoomRepository.findById(chatRoomId).orElseThrow(NoSuchElementException::new))
                .member(memberRepository.getReferenceById(chatRequestDTO.getUserId()))
                .description(chatRequestDTO.getMessage())
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
}
