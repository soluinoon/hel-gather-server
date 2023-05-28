package com.mate.helgather.domain;

import com.mate.helgather.domain.status.ChatRoomStatus;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruitment_id", nullable = false)
    private Recruitment recruitment;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ChatRoomStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom")
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();
}
