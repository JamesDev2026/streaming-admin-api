package com.streaming.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Account", schema = "dbo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAccount;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 45)
    private String userName;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 2000)
    private String description;

    private java.time.LocalDateTime purchaseDate;

    private java.time.LocalDateTime expirationDate;

    @Column(nullable = false)
    private java.math.BigDecimal price;

    @Column(nullable = false)
    private Integer maxUsers;

    // FK como campos simples (sin relaciones)
    private Integer idAccountType;
    private Integer idCategory;
    private Integer idPanel;
}
