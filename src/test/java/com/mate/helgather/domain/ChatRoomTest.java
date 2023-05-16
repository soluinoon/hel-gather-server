package com.mate.helgather.domain;

import com.mate.helgather.domain.status.ChatRoomStatus;
import com.mate.helgather.domain.status.MemberStatus;
import com.mate.helgather.domain.status.RecruitmentStatus;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.RecruitmentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class ChatRoomTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private RecruitmentRepository recruitmentRepository;
    private Member member;
    private Recruitment recruitment;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .userName("김지홍")
                .phone("01085422990")
                .password("a1349529")
                .birthDate(LocalDate.of(1998,3,31))
                .status(MemberStatus.ACTIVE)
                .build();
        memberRepository.save(member);
    }

    @DisplayName("챗룸 저장이 가능하다.")
    @Test
    void canSave() {
//         given
        member = Member.builder()
                .userName("김지홍")
                .phone("01085422990")
                .password("a1349529")
                .birthDate(LocalDate.of(1998,3,31))
                .status(MemberStatus.ACTIVE)
                .build();
        memberRepository.save(member);
//         when
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
//         then
        assertThat(savedChatRoom.getId()).isEqualTo(chatRoom.getId());
        assertThat(savedChatRoom.getRecruitment().getMember().getUserName()).isEqualTo("김지홍");
    }
}