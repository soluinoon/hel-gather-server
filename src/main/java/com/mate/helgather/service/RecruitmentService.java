package com.mate.helgather.service;

import com.mate.helgather.domain.ChatRoom;
import com.mate.helgather.domain.Member;
import com.mate.helgather.domain.MemberChatRoom;
import com.mate.helgather.domain.Recruitment;
import com.mate.helgather.domain.category.Location;
import com.mate.helgather.domain.status.ChatRoomStatus;
import com.mate.helgather.dto.RecruitmentChatResponseDto;
import com.mate.helgather.dto.RecruitmentListResponseDto;
import com.mate.helgather.dto.RecruitmentRequestDto;
import com.mate.helgather.dto.RecruitmentResponseDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.ChatRoomRepository;
import com.mate.helgather.repository.MemberChatRoomRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.repository.RecruitmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentService {
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

    @Transactional
    public void save(RecruitmentRequestDto recruitmentRequestDto) {
        Member member = memberRepository.findById(recruitmentRequestDto.getMemberId())
                .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR));

        Recruitment recruitment = recruitmentRequestDto.toEntity(member);

        Location location = Location.of(recruitment.getLocation(), recruitment.getSubLocation());

        recruitmentRepository.save(recruitment);
    }

    /**
     * 게시글 작성 -> 단체 채팅방 개설
     * 이 메서드는 발표전날 한 모집글에 한 단체 채팅방을 생각해서 만든 메서드 입니다.
     * @param recruitmentRequestDto
     */
    @Transactional
    public void saveV2(RecruitmentRequestDto recruitmentRequestDto) {
        Member member = memberRepository.findById(recruitmentRequestDto.getMemberId())
                .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR));

        Recruitment recruitment = recruitmentRequestDto.toEntity(member);

        Location location = Location.of(recruitment.getLocation(), recruitment.getSubLocation());

        Recruitment save = recruitmentRepository.save(recruitment);
        // 채팅방도 생성
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .recruitment(save)
                .status(ChatRoomStatus.ACTIVE)
                .build());
        // n : m에 저장
        memberChatRoomRepository.save(MemberChatRoom
                .builder()
                .member(member)
                .chatRoom(chatRoom)
                .build());
    }

    @Transactional
    public List<RecruitmentListResponseDto> findAllByCategory(Long locationNumber, Long subLocationNumber) {
        Location location = Location.of(locationNumber, subLocationNumber);

        // 날짜순으로 내림차순으로 정렬되서 줌
        // TODO: 만약, 멤버가 없어졌다면(탈퇴) 어떻게 되는지 알아보기.
        List<Recruitment> recruitments = recruitmentRepository.findAllByLocationAndSubLocationOrderByCreatedAtDesc(locationNumber, subLocationNumber);

        return recruitments.stream().map(RecruitmentListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RecruitmentListResponseDto> findAll() {
        // 날짜순으로 내림차순으로 정렬되서 줌
        List<Recruitment> recruitments = recruitmentRepository.findAll();

        return recruitments.stream().map(RecruitmentListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecruitmentResponseDto findById(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_RECRUITMENT_ERROR));
        return new RecruitmentResponseDto(recruitment);
    }

    @Transactional
    public RecruitmentChatResponseDto createChatRoom(Long recruitmentId, Long requestMemberId) {
        try {
            // TODO: 탈퇴한 회원일 때 예외처리
            Member referenceById = memberRepository.getReferenceById(requestMemberId);
            // 존재하는 모집글인지 확인
            Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                    .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_RECRUITMENT_ERROR));
            // 채팅방 하나 만들기
            ChatRoom savedChatRoom = chatRoomRepository.save(ChatRoom.builder()
                    .recruitment(recruitment)
                    .status(ChatRoomStatus.ACTIVE)
                    .build());
            // n:m 연관관계 테이블에 저장
            memberChatRoomRepository.save(MemberChatRoom
                            .builder()
                            .member(referenceById)
                            .chatRoom(savedChatRoom)
                            .build());
            memberChatRoomRepository.save(MemberChatRoom
                            .builder()
                            .member(recruitment.getMember())
                            .chatRoom(savedChatRoom)
                            .build());
            // DTO 반환
            return new RecruitmentChatResponseDto(savedChatRoom.getId(), recruitment.getMember().getId(), requestMemberId);
        } catch (EntityNotFoundException e) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
    }

    /**
     * 이 메서드는 발표 전날 게시글이 단체채팅방으로 관리되는 방법 때문에 만든 것 입니다.
     * @param recruitmentId
     * @param requestMemberId
     * @return
     */
    @Transactional
    public RecruitmentChatResponseDto apply(Long recruitmentId, Long requestMemberId) {
        try {
            // TODO: 탈퇴한 회원일 때 예외처리
            Member referenceById = memberRepository.getReferenceById(requestMemberId);
            // 존재하는 모집글인지 확인
            Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                    .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_RECRUITMENT_ERROR));
            // 채팅방 하나 만들기
            // 현재 n:m이라 오류 발생 가능(채팅방 : 리크루트먼트)
            ChatRoom chatRoom = chatRoomRepository.findByRecruitment(recruitment)
                    // 추후 다른 에러코드로 바꿀 것
                    .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_CHATROOM_ERROR));
            // n:m 연관관계 테이블에 저장
            memberChatRoomRepository.save(MemberChatRoom
                    .builder()
                    .member(referenceById)
                    .chatRoom(chatRoom)
                    .build());
            // DTO 반환
            return new RecruitmentChatResponseDto(chatRoom.getId(), recruitment.getMember().getId(), requestMemberId);
        } catch (EntityNotFoundException e) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
    }

    @Transactional
    public List<RecruitmentListResponseDto> findByOptions(RecruitmentOptions recruitmentOptions) {
        Location location = Location.of(recruitmentOptions.getLocation(), recruitmentOptions.getSubLocation());
        // TODO: 만약, 멤버가 없어졌다면(탈퇴) 어떻게 되는지 알아보기.
        List<Recruitment> recruitments = recruitmentRepository.findAllByRecruitmentOptions(recruitmentOptions);

        return recruitments.stream()
                .map(RecruitmentListResponseDto::new)
                .collect(Collectors.toList());
    }
}
