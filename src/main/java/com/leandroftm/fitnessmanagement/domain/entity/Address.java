package com.leandroftm.fitnessmanagement.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "addresses")

public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String street;
    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, length = 10)
    private String number;
    @Size(max = 50)
    @Column(length = 50)
    private String complement;
    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String city;
    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String state;
    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, length = 10)
    private String zipCode;

}
