package com.mate.helgather.service;

import com.mate.helgather.domain.*;
import com.mate.helgather.domain.status.ChatRoomStatus;
import com.mate.helgather.domain.status.MemberStatus;
import com.mate.helgather.domain.status.RecruitmentStatus;
import com.mate.helgather.dto.ChatDto;
import com.mate.helgather.repository.ApplicationRepository;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.mate.helgather.dto.MessagesResponse;
import com.mate.helgather.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;
    private final RecruitmentRepository recruitmentRepository;


    public void saveMessage(ChatDto chatDTO) {
        messageRepository.save(Message.builder().chatRoom(chatRoomRepository.findById(chatDTO.getRoomId()).orElseThrow(NoSuchElementException::new))
                .member(memberRepository.getReferenceById(chatDTO.getUserId()))
                .description(chatDTO.getMessage())
                .build());
    }

    public List<MessagesResponse> getMessages(Long chatRoomId) {
        List<Message> messages = messageRepository.findAllByChatRoom_IdOrderByCreatedAt(chatRoomId);
        List<MessagesResponse> messagesResponses = new ArrayList<>();

        for (Message message : messages) {
            messagesResponses.add(new MessagesResponse(message.getMember().getId(),
                    0,
                    message.getDescription(),
                    message.getCreatedAt().toString(),
                    false));
        }
        return messagesResponses;
    }

    public void testChat() {
        Member member1 = Member.builder()
                .userName("김지홍")
                .nickname("jihonkim")
                .password("11")
                .phone("01085422990")
                .birthDate(LocalDate.of(1998, 03, 31))
                .status(MemberStatus.ACTIVE)
                .build();
        Member member2 = Member.builder()
                .userName("노균욱")
                .nickname("gyroh")
                .password("11")
                .phone("0109991290")
                .birthDate(LocalDate.of(1997, 04, 13))
                .status(MemberStatus.ACTIVE)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        Recruitment recruitment = Recruitment.builder()
                .member(member1)
                .title("3대 500 운동파트너 구합니다")
                .description("ㅈㄱㄴ")
                .status(RecruitmentStatus.ACTIVE)
                .build();
        recruitmentRepository.save(recruitment);

        ChatRoom chatRoom = ChatRoom.builder()
                .recruitment(recruitment)
                .status(ChatRoomStatus.ACTIVE)
                .build();
        chatRoomRepository.save(chatRoom);

        MemberChatRoom memberChatRoom1 = MemberChatRoom.builder()
                .chatRoom(chatRoom)
                .member(member1)
                .build();
        MemberChatRoom memberChatRoom2 = MemberChatRoom.builder()
                .chatRoom(chatRoom)
                .member(member2)
                .build();
        memberChatRoomRepository.save(memberChatRoom1);
        memberChatRoomRepository.save(memberChatRoom2);
    }
}
