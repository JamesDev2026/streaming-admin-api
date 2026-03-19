package com.streaming.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AccountSubscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAccountDetail;

    private Integer idAccount;
    private Integer idAppUser;

    @Column(nullable = false)
    private java.math.BigDecimal amountPaid;

    @Column(nullable = false)
    private java.time.LocalDateTime expirationDate;

    @Column(nullable = false, length = 20)
    private String paymentStatus;

    @Column(nullable = false)
    private java.time.LocalDateTime startDate;

    @Column(nullable = false)
    private java.time.LocalDateTime paymentDate;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(length = 900)
    private String description;
}
