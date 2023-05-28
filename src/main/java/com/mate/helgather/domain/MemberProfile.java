package com.mate.helgather.domain;

import com.mate.helgather.domain.status.MemberProfileStatus;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String imageUrl;

    private String introduction;

    private Integer benchPress;

    private Integer squat;

    private Integer deadLift;

    @Builder.Default
    @Column(name = "exercise_count")
    private Integer exerciseCount = 0;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @NotNull
    private MemberProfileStatus status = MemberProfileStatus.ACTIVE;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setBenchPress(Integer benchPress) {
        this.benchPress = benchPress;
    }

    public void setSquat(Integer squat) {
        this.squat = squat;
    }

    public void setDeadLift(Integer deadLift) {
        this.deadLift = deadLift;
    }
}
