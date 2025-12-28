package com.leandroftm.fitnessmanagement.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "training_program_exercises",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_training_program_unique",
                        columnNames = {"training_program_id", "exercise_id"}
                )
        }
)
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class TrainingProgramExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "training_program_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_program_exercise_program")
    )

    private TrainingProgram trainingProgram;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "exercise_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_program_exercise_exercise")
    )
    private Exercise exercise;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private int exerciseOrder;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
