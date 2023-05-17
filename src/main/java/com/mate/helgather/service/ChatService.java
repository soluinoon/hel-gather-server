package com.mate.helgather.service;

import com.mate.helgather.domain.*;
import com.mate.helgather.domain.status.ChatRoomStatus;
import com.mate.helgather.domain.status.MemberStatus;
import com.mate.helgather.domain.status.RecruitmentStatus;
import com.mate.helgather.dto.ChatDto;
import com.mate.helgather.dto.ChatRoomDto;
import com.mate.helgather.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;
    private final RecruitmentRepository recruitmentRepository;
    /**
     채팅방을 크루지원 id값을 이용해 가져온다.
     만약, 채팅방이 없다면 예외를 발생
     멤버가 자기 페이지에서 채팅을 들어갈 때 사용
     */
//    @Transactional
//    public ChatRoomDto getChatRoomsByMemberId(Long memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(NoSuchElementException::new);
//        return new ChatRoomDto(member.getChatRoom().getId());
//    }

    /**
    채팅방을 지원서를 이용해 만든다.
    지원서를 참고해 지원한 유저와 모집글의 유저로 채팅방을 만든 뒤,
    각각의 유저의 채팅방을 설정해준다.

    이 메서드는 아마 지원서의 지원과 수락을 받는다면 일어날 것이다.
     */
//    @Transactional
//    public ChatRoom createChatRoom(Long applicationId) {
//        Application application = applicationRepository.findById(applicationId).orElseThrow(IllegalAccessError::new);
//        //
//        Member recruitMember = application.getRecruitment().getMember();
//        Member applyMember = application.getMember();
//
//        ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.builder()
//                .recruitment(application.getRecruitment())
//                .build());
//        recruitMember.setChatRoom(savedChatRoom);
//        applyMember.setChatRoom(savedChatRoom);
//        return savedChatRoom;
//    }

    public void saveMessage(ChatDto chatDTO) {
        messageRepository.save(Message.builder().chatRoom(chatRoomRepository.findById(chatDTO.getRoomId()).orElseThrow(NoSuchElementException::new))
                .member(memberRepository.getReferenceById(chatDTO.getUserId()))
                .description(chatDTO.getMessage())
                .build());
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
