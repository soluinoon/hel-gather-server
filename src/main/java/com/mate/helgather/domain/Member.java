package com.mate.helgather.domain;

import com.mate.helgather.domain.status.MemberStatus;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "chat_room_id", nullable = true)
    private ChatRoom chatRoom;
    @NotNull
    private String userName;
    @NotNull
    private String phone;
    @NotNull
    private String password;
    @DateTimeFormat
    @NotNull
    private String birthDate;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private MemberStatus status;

    @OneToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
    @OneToOne
    @JoinColumn(name = "social_login_id")
    private SocialLogin socialLogin;
    @OneToOne
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;
    @OneToOne
    private Application application;
    @OneToMany(mappedBy = "member")
    private List<Message> messages = new ArrayList<>();
}
