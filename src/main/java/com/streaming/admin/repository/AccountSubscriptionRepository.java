package com.streaming.admin.repository;

import com.streaming.admin.entity.AccountSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountSubscriptionRepository extends JpaRepository<AccountSubscription, Integer> {

    // Buscar suscripción activa por cuenta y usuario
    // Search active subscription by each account and user
    Optional<AccountSubscription> findByIdAccountAndIdAppUserAndIsActiveTrue(
            Integer idAccount,
            Integer idAppUser
    );

    // Contar usuarios activos en una cuenta (para límite de 3)
    // Count active users in an account (for limit of 3)
    long countByIdAccountAndIsActiveTrue(Integer idAccount);

    boolean existsByIdAppUser(Integer idAppUser);

    boolean existsByIdAccount(Integer idAccount);

}