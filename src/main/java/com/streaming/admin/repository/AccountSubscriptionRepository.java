package com.streaming.admin.repository;

import com.streaming.admin.entity.AccountSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountSubscriptionRepository extends JpaRepository<AccountSubscription, Integer> {

    Optional<AccountSubscription> findByIdAccountAndIdAppUserAndIsActiveTrue(
            Integer idAccount,
            Integer idAppUser
    );

    long countByIdAccountAndIsActiveTrue(Integer idAccount);

    boolean existsByIdAppUser(Integer idAppUser);

    boolean existsByIdAccount(Integer idAccount);

    boolean existsByIdAccountAndIdAppUserAndIsActiveTrue(Integer idAccount, Integer idAppUser);

    List<AccountSubscription> findByIdAccountAndIsActiveTrue(Integer idAccount);

    List<AccountSubscription> findByIdAppUser(Integer idAppUser);

    List<AccountSubscription> findByPaymentStatusIgnoreCaseAndIsActiveTrue(String paymentStatus);

    List<AccountSubscription> findByIsActiveTrue();
}
