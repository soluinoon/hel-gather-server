package com.mate.helgather.domain;

import com.mate.helgather.domain.status.ChatRoomStatus;
import com.mate.helgather.domain.status.MemberStatus;
import com.mate.helgather.domain.status.RecruitmentStatus;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.RecruitmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class RecruitmentTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RecruitmentRepository recruitmentRepository;
    private Member member;
    private Recruitment recruitment;

    @Test
    void canSave() {
        // given
        member = Member.builder()
                .userName("김지홍")
                .nickname("king")
                .phone("01085422990")
                .password("a1349529")
                .birthDate(LocalDate.of(1998,3,31))
                .status(MemberStatus.ACTIVE)
                .build();
        Member savedMember = memberRepository.save(member);

        recruitment = Recruitment.builder()
                .id(10L)
                .member(member)
                .title("3대 500 운동파트너 구합니다")
                .description("ㅈㄱㄴ")
                .status(RecruitmentStatus.ACTIVE)
                .build();
        // when
        Recruitment savedRecruitment = recruitmentRepository.save(recruitment);
        // then
        assertThat(recruitment.getId()).isEqualTo(10L);
    }

    @Test
    void canfindById() {
        // given
        member = Member.builder()
                .id(1L)
                .userName("김지홍")
                .phone("01085422990")
                .nickname("king")
                .password("a1349529")
                .birthDate(LocalDate.of(1998,3,31))
                .status(MemberStatus.ACTIVE)
                .build();
        memberRepository.save(member);

        recruitment = Recruitment.builder()
                .id(1L)
                .member(member)
                .title("3대 500 운동파트너 구합니다")
                .description("ㅈㄱㄴ")
                .status(RecruitmentStatus.ACTIVE)
                .build();
        // when
        Recruitment save = recruitmentRepository.save(recruitment);
        List<Recruitment> recruitmentRepositoryAll = recruitmentRepository.findAll();
        // then
        assertThat(recruitmentRepositoryAll).isNotEqualTo(null);
        assertThat(recruitmentRepository.findById(1L).get()).isNotNull();
    }
}