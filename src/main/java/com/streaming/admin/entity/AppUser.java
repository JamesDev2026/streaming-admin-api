package com.streaming.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AppUser")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAppUser;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 45)
    private String lastName;

    @Column(length = 45)
    private String nickName;

    @Column(length = 20)
    private String ci;

    private java.time.LocalDate dateOfBirth;

    @Column(nullable = false, length = 20)
    private String phoneNumber;
}
