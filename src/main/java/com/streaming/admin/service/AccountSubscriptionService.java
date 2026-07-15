package com.streaming.admin.service;

import com.streaming.admin.dto.request.AccountSubscriptionRequestDto;
import com.streaming.admin.dto.request.ChangeAccountRequestDto;
import com.streaming.admin.dto.request.UpdatePaymentStatusRequestDto;
import com.streaming.admin.dto.response.CountActiveUsersResponseDto;
import com.streaming.admin.entity.Account;
import com.streaming.admin.entity.AccountSubscription;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.AccountSubscriptionRepository;
import com.streaming.admin.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountSubscriptionService {

    public static final String PAYMENT_PAID = "Pagado";
    public static final String PAYMENT_PENDING = "Pendiente";

    private static final Set<String> VALID_PAYMENT_STATUSES = Set.of(PAYMENT_PAID, PAYMENT_PENDING);

    private final AccountSubscriptionRepository subscriptionRepository;
    private final AccountRepository accountRepository;
    private final AppUserRepository appUserRepository;

    public AccountSubscriptionService(
            AccountSubscriptionRepository subscriptionRepository,
            AccountRepository accountRepository,
            AppUserRepository appUserRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.accountRepository = accountRepository;
        this.appUserRepository = appUserRepository;
    }

    public List<AccountSubscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public List<AccountSubscription> findActive() {
        return subscriptionRepository.findByIsActiveTrue();
    }

    public AccountSubscription findById(Integer id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
    }

    public Optional<AccountSubscription> getActiveSubscription(Integer idAccount, Integer idAppUser) {
        return subscriptionRepository.findByIdAccountAndIdAppUserAndIsActiveTrue(idAccount, idAppUser);
    }

    public List<AccountSubscription> findActiveByAccount(Integer idAccount) {
        validateAccountExists(idAccount);
        return subscriptionRepository.findByIdAccountAndIsActiveTrue(idAccount);
    }

    public List<AccountSubscription> findByAppUser(Integer idAppUser) {
        validateAppUserExists(idAppUser);
        return subscriptionRepository.findByIdAppUser(idAppUser);
    }

    public List<AccountSubscription> findPendingPayments() {
        return subscriptionRepository.findByPaymentStatusIgnoreCaseAndIsActiveTrue(PAYMENT_PENDING);
    }

    public CountActiveUsersResponseDto countActiveUsers(Integer idAccount) {
        Account account = findAccount(idAccount);
        long activeUsers = subscriptionRepository.countByIdAccountAndIsActiveTrue(idAccount);

        return CountActiveUsersResponseDto.builder()
                .idAccount(idAccount)
                .activeUsersCount(activeUsers)
                .maxUsers(account.getMaxUsers())
                .availableSlots(Math.max(0, account.getMaxUsers() - (int) activeUsers))
                .build();
    }

    public AccountSubscription create(AccountSubscriptionRequestDto request) {
        validateAccountExists(request.getIdAccount());
        validateAppUserExists(request.getIdAppUser());
        validatePaymentStatus(request.getPaymentStatus());
        validateNoDuplicateActiveAssignment(request.getIdAccount(), request.getIdAppUser());
        validateAvailableSlot(request.getIdAccount());

        LocalDateTime now = LocalDateTime.now();

        AccountSubscription subscription = AccountSubscription.builder()
                .idAccount(request.getIdAccount())
                .idAppUser(request.getIdAppUser())
                .amountPaid(request.getAmountPaid())
                .expirationDate(request.getExpirationDate())
                .paymentStatus(normalizePaymentStatus(request.getPaymentStatus()))
                .startDate(request.getStartDate() != null ? request.getStartDate() : now)
                .paymentDate(request.getPaymentDate() != null ? request.getPaymentDate() : now)
                .isActive(true)
                .description(request.getDescription())
                .build();

        return subscriptionRepository.save(subscription);
    }

    public AccountSubscription update(Integer id, AccountSubscriptionRequestDto request) {
        AccountSubscription subscription = findById(id);

        validateAccountExists(request.getIdAccount());
        validateAppUserExists(request.getIdAppUser());
        validatePaymentStatus(request.getPaymentStatus());

        boolean accountChanged = !request.getIdAccount().equals(subscription.getIdAccount());
        boolean userChanged = !request.getIdAppUser().equals(subscription.getIdAppUser());

        if (accountChanged || userChanged) {
            validateNoDuplicateActiveAssignment(request.getIdAccount(), request.getIdAppUser());
        }

        if (accountChanged && Boolean.TRUE.equals(subscription.getIsActive())) {
            validateAvailableSlot(request.getIdAccount());
        }

        subscription.setIdAccount(request.getIdAccount());
        subscription.setIdAppUser(request.getIdAppUser());
        subscription.setAmountPaid(request.getAmountPaid());
        subscription.setExpirationDate(request.getExpirationDate());
        subscription.setPaymentStatus(normalizePaymentStatus(request.getPaymentStatus()));
        if (request.getStartDate() != null) {
            subscription.setStartDate(request.getStartDate());
        }
        if (request.getPaymentDate() != null) {
            subscription.setPaymentDate(request.getPaymentDate());
        }
        subscription.setDescription(request.getDescription());

        return subscriptionRepository.save(subscription);
    }

    public AccountSubscription changeAccount(Integer id, ChangeAccountRequestDto request) {
        AccountSubscription subscription = findById(id);
        Integer newAccountId = request.getIdAccount();

        if (newAccountId.equals(subscription.getIdAccount())) {
            return subscription;
        }

        validateAccountExists(newAccountId);
        validateNoDuplicateActiveAssignment(newAccountId, subscription.getIdAppUser());

        if (Boolean.TRUE.equals(subscription.getIsActive())) {
            validateAvailableSlot(newAccountId);
        }

        subscription.setIdAccount(newAccountId);
        return subscriptionRepository.save(subscription);
    }

    public AccountSubscription updatePaymentStatus(Integer id, UpdatePaymentStatusRequestDto request) {
        AccountSubscription subscription = findById(id);
        validatePaymentStatus(request.getPaymentStatus());

        String status = normalizePaymentStatus(request.getPaymentStatus());
        subscription.setPaymentStatus(status);

        if (PAYMENT_PAID.equalsIgnoreCase(status)) {
            subscription.setPaymentDate(LocalDateTime.now());
        }

        return subscriptionRepository.save(subscription);
    }

    public AccountSubscription deactivate(Integer id) {
        AccountSubscription subscription = findById(id);
        subscription.setIsActive(false);
        return subscriptionRepository.save(subscription);
    }

    public void delete(Integer id) {
        findById(id);
        subscriptionRepository.deleteById(id);
    }

    private Account findAccount(Integer idAccount) {
        return accountRepository.findById(idAccount)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    private void validateAccountExists(Integer idAccount) {
        if (!accountRepository.existsById(idAccount)) {
            throw new RuntimeException("Account not found");
        }
    }

    private void validateAppUserExists(Integer idAppUser) {
        if (!appUserRepository.existsById(idAppUser)) {
            throw new RuntimeException("AppUser not found");
        }
    }

    private void validatePaymentStatus(String paymentStatus) {
        if (paymentStatus == null || !VALID_PAYMENT_STATUSES.contains(normalizePaymentStatus(paymentStatus))) {
            throw new RuntimeException("Payment status must be '" + PAYMENT_PAID + "' or '" + PAYMENT_PENDING + "'");
        }
    }

    private String normalizePaymentStatus(String paymentStatus) {
        if (PAYMENT_PAID.equalsIgnoreCase(paymentStatus)) {
            return PAYMENT_PAID;
        }
        if (PAYMENT_PENDING.equalsIgnoreCase(paymentStatus)) {
            return PAYMENT_PENDING;
        }
        return paymentStatus;
    }

    private void validateNoDuplicateActiveAssignment(Integer idAccount, Integer idAppUser) {
        if (subscriptionRepository.existsByIdAccountAndIdAppUserAndIsActiveTrue(idAccount, idAppUser)) {
            throw new RuntimeException("The user is already assigned to this account");
        }
    }

    private void validateAvailableSlot(Integer idAccount) {
        Account account = findAccount(idAccount);
        long activeUsers = subscriptionRepository.countByIdAccountAndIsActiveTrue(idAccount);

        if (activeUsers >= account.getMaxUsers()) {
            throw new RuntimeException(
                    "The account already has the maximum users allowed (" + account.getMaxUsers() + ")"
            );
        }
    }
}
