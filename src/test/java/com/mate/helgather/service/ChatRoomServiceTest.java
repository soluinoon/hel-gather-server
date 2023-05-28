package com.mate.helgather.service;

import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.RecruitmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    private MemberChatRoomRepository memberChatRoomRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RecruitmentRepository recruitmentRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;



//    @Test
//    @DisplayName("멤버 id를 통해 챗룸 리스트들을 가져올 수 있다.")
//    void canFindChatRoomListByMemberId() {
//    // given
//    Member member1 = Member.builder()
//            .userName("김지홍")
//            .nickname("jihonkim")
//            .password("11")
//            .phone("01085422990")
//            .birthDate(LocalDate.of(1998, 03, 31))
//            .status(MemberStatus.ACTIVE)
//            .build();
//    Member member2 = Member.builder()
//            .userName("노균욱")
//            .nickname("gyroh")
//            .password("11")
//            .phone("0109991290")
//            .birthDate(LocalDate.of(1997, 04, 13))
//            .status(MemberStatus.ACTIVE)
//            .build();
//    memberRepository.save(member1);
//    memberRepository.save(member2);
//
//    Recruitment recruitment = Recruitment.builder()
//            .member(member1)
//            .title("3대 500 운동파트너 구합니다")
//            .description("ㅈㄱㄴ")
//            .status(RecruitmentStatus.ACTIVE)
//            .build();
//    recruitmentRepository.save(recruitment);
//
//    ChatRoom chatRoom = ChatRoom.builder()
//            .recruitment(recruitment)
//            .status(ChatRoomStatus.ACTIVE)
//            .build();
//    chatRoomRepository.save(chatRoom);
//
//    MemberChatRoom memberChatRoom1 = MemberChatRoom.builder()
//            .chatRoom(chatRoom)
//            .member(member1)
//            .build();
//    MemberChatRoom memberChatRoom2 = MemberChatRoom.builder()
//            .chatRoom(chatRoom)
//            .member(member2)
//            .build();
//    memberChatRoomRepository.save(memberChatRoom1);
//    memberChatRoomRepository.save(memberChatRoom2);
//    // when
//    List<ChatRoomListResponse> chatRooms = chatRoomService.getChatRoomsByMemberId(member1.getId());
//    // then
//    assertThat(chatRooms).allSatisfy(user -> {
//        // 채팅방 리스트의 유저들은 당사자가 아니다.
//        assertThat(user.getId()).isNotEqualTo(member1.getNickname());
//    });
//    }
}