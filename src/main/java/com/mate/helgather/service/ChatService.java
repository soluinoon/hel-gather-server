package com.mate.helgather.service;

import com.mate.helgather.domain.Message;
import com.mate.helgather.dto.ChatRequestDto;
import com.mate.helgather.dto.MessagesResponse;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    public Message saveMessage(ChatRequestDto chatRequestDTO, Long chatRoomId) {
        return messageRepository.save(Message.builder().chatRoom(chatRoomRepository.findById(chatRoomId).orElseThrow(NoSuchElementException::new))
                .member(memberRepository.getReferenceById(chatRequestDTO.getUserId()))
                .description(chatRequestDTO.getMessage())
                .build());
    }

    public List<MessagesResponse> getMessages(Long chatRoomId, Long requestUserId) throws BaseException {
        if (!chatRoomRepository.existsById(chatRoomId)) {
            throw new BaseException(ErrorCode.NO_SUCH_CHATROOM_ERROR);
        }
        if (!memberRepository.existsById(requestUserId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
        // TODO: 채팅방에 없는 유저가 넘어올 때도 예외처리 필요
        List<Message> messages = messageRepository.findAllByChatRoom_IdOrderByCreatedAt(chatRoomId);
        List<MessagesResponse> messagesResponses = new ArrayList<>();

        for (Message message : messages) {
            messagesResponses.add(new MessagesResponse(message, requestUserId));
        }

        return messagesResponses;
    }
}
