package com.mate.helgather.domain;

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
public class User {
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
    @OneToMany(mappedBy = "user")
    private List<Message> messages = new ArrayList<>();
}
