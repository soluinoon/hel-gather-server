package com.mate.helgather.repository;

import com.mate.helgather.domain.MemberChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long> {
    @Query("SELECT c FROM MemberChatRoom c WHERE c.chatRoom IN (SELECT c1.chatRoom FROM MemberChatRoom c1 WHERE c1.member.id = :memberId) AND c.member.id <> :memberId")
    List<MemberChatRoom> findAllByMemberIdWithCond(@Param("memberId") Long memberId);
    List<MemberChatRoom> findAllByMemberId(Long memberId);
    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberID);

}
