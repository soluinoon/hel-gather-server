package com.mate.helgather.service;

import com.mate.helgather.domain.MemberChatRoom;
import com.mate.helgather.domain.Message;
import com.mate.helgather.domain.Recruitment;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private static final String CHATROOM_IMAGE_PATH = "http://hel-gather.s3.ap-northeast-2.amazonaws.com/images/istockphoto-1277134944-170667a.jpg";

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

    public List<MessagesResponseDto> findMessages(Long chatRoomId, Long requestUserId, Pageable pageable) throws BaseException {
        if (!chatRoomRepository.existsById(chatRoomId)) {
            throw new BaseException(ErrorCode.NO_SUCH_CHATROOM_ERROR);
        }
        if (!memberRepository.existsById(requestUserId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
        if (!memberChatRoomRepository.existsByChatRoomIdAndMemberId(chatRoomId, requestUserId)) {
            throw new BaseException(ErrorCode.MEMBER_NOT_EXIST_IN_ROOM);
        }

        Slice<Message> messages = messageRepository.findSlicesByChatRoom_Id(chatRoomId, pageable);

        return messages.stream()
                .map(message -> new MessagesResponseDto(message, requestUserId))
                .collect(Collectors.toList());
    }
    /*
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
     */

    /**
     * 채팅방을 멤버 아이디로 찾는다.
     * 발표 전날 만든 메서드이며, 차이점은 채팅방 기본 이미지, 채팅방 이름은 recruitment 제목으로
     * 마지막으로 단체채팅방을 가정하고 만든다.
     * @param id
     * @return
     * @throws BaseException
     */
    public List<ChatRoomListResponseDto> findChatRoomsByMemberIdV2(Long id) throws BaseException {
        if (!memberRepository.existsById(id)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
        List<MemberChatRoom> memberChatRooms = memberChatRoomRepository.findAllByMemberId(id);
        List<ChatRoomListResponseDto> chatRoomListResponsDtos = new ArrayList<>();

        for (MemberChatRoom memberChatRoom : memberChatRooms) {
            // 해당하는 모집글 찾기
            Recruitment recruitment = memberChatRoom.getChatRoom().getRecruitment();
            // 가장 최근 메세지 찾기
            Message recentMessage = messageRepository.findTopByChatRoomOrderByCreatedAtDesc(memberChatRoom.getChatRoom())
                    .orElse(Message.builder()
                            .chatRoom(memberChatRoom.getChatRoom())
                            .description("아직 채팅이 없습니다.")
                            .member(null)
                            .createdAt(LocalDateTime.now())
                            .build());
            chatRoomListResponsDtos.add(new ChatRoomListResponseDto(recruitment.getTitle(),
                    recruitment.getMember().getNickname(),
                    CHATROOM_IMAGE_PATH,
                    recentMessage.getCreatedAt().toString(),
                    recentMessage.getDescription(),
                    memberChatRoom.getChatRoom().getId()));
        }
        return chatRoomListResponsDtos;
    }
}
