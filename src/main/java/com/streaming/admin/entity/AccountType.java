package com.streaming.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AccountType", schema = "dbo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAccountType;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(length = 990)
    private String description;
}