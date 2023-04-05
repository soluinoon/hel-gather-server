package com.mate.helgather.domain;

import com.mate.helgather.domain.status.RecruitmentStatus;
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
public class Recruitment {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @NotNull
    private String title;
    private String description;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;
    @Enumerated(EnumType.STRING)
    @NotNull
    private RecruitmentStatus status;

    @OneToMany(mappedBy = "recruitment")
    private List<Application> applications = new ArrayList<>();
}
