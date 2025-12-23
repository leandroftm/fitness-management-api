package com.leandroftm.fitnessmanagement.entity;

import com.leandroftm.fitnessmanagement.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "students",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_student_email",
                        columnNames = "email"
                )
        }
)

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @NotBlank
    @Column(nullable = false, length = 255)
    @ToString.Include
    private String fullName;
    @NotBlank
    @Email
    @Column(nullable = false, length = 255)
    private String email;
    @NotBlank
    @Column(nullable = false, length = 20)
    @ToString.Include
    private String phoneNumber;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @ToString.Include
    private Gender gender;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "address_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "FK_student_address")
    )
    private Address address;
}
