package com.mate.helgather.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLogin {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @NotNull
    private String provider;
    @NotNull
    private Long provider_id;
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;
    @DateTimeFormat
    @NotNull
    private LocalDateTime expiredAt;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;
}
