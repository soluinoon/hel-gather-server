package com.mate.helgather.domain;

import com.mate.helgather.domain.status.ExerciseCategory;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('DEAD_LIFT', 'SQUAT', 'BENCH_PRESS', 'TODAY')")
    @NotNull
    private ExerciseCategory category;

    @Nullable
    private String videoUrl;

    @Nullable
    private String thumbnailUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
