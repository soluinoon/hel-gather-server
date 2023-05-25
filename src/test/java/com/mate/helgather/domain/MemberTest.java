package com.mate.helgather.domain;

import com.mate.helgather.domain.status.MemberStatus;
import com.mate.helgather.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class MemberTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @Test
    void canSave() {
        // given
        member = Member.builder()
                .name("김지홍")
                .nickname("king")
                .phone("01085422990")
                .password("a1349529")
                .birthDate(LocalDate.of(1998,3,31))
                .status(MemberStatus.ACTIVE)
                .build();
        // when
        Member savedMember = memberRepository.save(member);
        // then
        assertThat(savedMember.getName()).isEqualTo("김지홍");
        assertThat(savedMember.getPhone()).isEqualTo(member.getPhone());
        assertThat(savedMember.getBirthDate().toString()).isEqualTo("1998-03-31");
    }

    @Test
    void canFindById() {
        // given
        member = Member.builder()
                .name("김지홍")
                .nickname("king")
                .phone("01085422990")
                .password("a1349529")
                .birthDate(LocalDate.of(1998,3,31))
                .status(MemberStatus.ACTIVE)
                .build();
        // when
        Member savedMember = memberRepository.save(member);
        // then
        assertThat(memberRepository.findById(1L).get()).isEqualTo(savedMember);
    }
}