package com.mate.helgather.domain;

import com.mate.helgather.domain.status.ChatRoomStatus;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @GeneratedValue
    @NotNull
    private Long id;
    @OneToOne
    @JoinColumn(name = "recruitment_id", nullable = false)
    private Recruitment recruitment;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ChatRoomStatus status;

    @OneToOne
    private Member member;
    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages = new ArrayList<>();
}
