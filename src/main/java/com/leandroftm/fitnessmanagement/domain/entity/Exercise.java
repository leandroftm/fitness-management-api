package com.leandroftm.fitnessmanagement.domain.entity;

import com.leandroftm.fitnessmanagement.domain.enums.ExerciseStatus;
import com.leandroftm.fitnessmanagement.domain.enums.MuscleGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "exercises",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_exercise_name",
                        columnNames = "name"
                )
        }
)
@NoArgsConstructor
@Getter
@Setter
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;
    @NotBlank
    @Column(nullable = false, length = 500)
    private String description;
    @NotBlank
    @Column(nullable = false, length = 255)
    private String videoUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MuscleGroup muscleGroup;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExerciseStatus status;
    @Column(nullable = false ,updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    private LocalDateTime deactivatedAt;

    @PrePersist
    public void prePersist() {
        this.status = ExerciseStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
