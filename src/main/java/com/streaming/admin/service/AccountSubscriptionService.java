package com.streaming.admin.service;

import com.streaming.admin.entity.AccountSubscription;
import com.streaming.admin.repository.AccountSubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountSubscriptionService {

    private final AccountSubscriptionRepository repository;

    public AccountSubscriptionService(AccountSubscriptionRepository repository) {
        this.repository = repository;
    }

    // Get active subscription by account and user
    public Optional<AccountSubscription> getActiveSubscription(Integer idAccount, Integer idAppUser) {
        return repository.findByIdAccountAndIdAppUserAndIsActiveTrue(idAccount, idAppUser);
    }

    // Count active users by account
    public long countActiveUsers(Integer idAccount) {
        return repository.countByIdAccountAndIsActiveTrue(idAccount);
    }

    // Create or save subscriptions
    public AccountSubscription save(AccountSubscription subscription) {

        // Validation: max 3 active users
        if (subscription.getIsActive()) {
            long activeUsers = repository.countByIdAccountAndIsActiveTrue(subscription.getIdAccount());

            if (activeUsers >= 3) {
                throw new RuntimeException("The account already has the max users actives (3)");
            }
        }

        return repository.save(subscription);
    }

    // Deactivate subscriptions
    public void deactivate(Integer id) {
        AccountSubscription subscription = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        subscription.setIsActive(false);
        repository.save(subscription);
    }
}