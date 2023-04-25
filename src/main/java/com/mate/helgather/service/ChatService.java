package com.mate.helgather.service;

import com.mate.helgather.domain.Application;
import com.mate.helgather.domain.ChatRoom;
import com.mate.helgather.domain.Member;
import com.mate.helgather.domain.Recruitment;
import com.mate.helgather.dto.ChatRoomDTO;
import com.mate.helgather.repository.ApplicationRepository;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.MessageRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;

    // 채팅방을 크루지원 id값을 이용해 가져온다.
    // 만약, 채팅방이 없다면 새로 만들어 줌.
    public ChatRoomDTO getChatRoom(Member member) {
        return null;
    }

    /*
    채팅방을 지원서를 이용해 만든다.
    지원서를 참고해 지원한 유저와 모집글의 유저로 채팅방을 만든 뒤,
    각각의 유저의 채팅방을 설정해준다.

    이 메서드는 아마 지원서의 지원과 수락을 받는다면 일어날 것이다.
     */
    @Transactional
    public ChatRoom createChatRoom(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(IllegalAccessError::new);
        //
        Member recruitMember = application.getRecruitment().getMember();
        Member applyMember = application.getMember();

        ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.builder()
                .recruitment(application.getRecruitment())
                .build());
        recruitMember.setChatRoom(savedChatRoom);
        applyMember.setChatRoom(savedChatRoom);
        return savedChatRoom;
    }
}
