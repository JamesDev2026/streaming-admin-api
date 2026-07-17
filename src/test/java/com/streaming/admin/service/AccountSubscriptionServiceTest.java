package com.streaming.admin.service;

import com.streaming.admin.dto.request.AccountSubscriptionRequestDto;
import com.streaming.admin.dto.request.ChangeAccountRequestDto;
import com.streaming.admin.dto.request.RenewSubscriptionRequestDto;
import com.streaming.admin.dto.request.UpdatePaymentStatusRequestDto;
import com.streaming.admin.dto.response.CountActiveUsersResponseDto;
import com.streaming.admin.entity.Account;
import com.streaming.admin.entity.AccountSubscription;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.AccountSubscriptionRepository;
import com.streaming.admin.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountSubscriptionServiceTest {

    @Mock
    private AccountSubscriptionRepository subscriptionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AccountSubscriptionService service;

    private AccountSubscriptionRequestDto validRequest() {
        return AccountSubscriptionRequestDto.builder()
                .idAccount(1)
                .idAppUser(10)
                .amountPaid(new BigDecimal("25.00"))
                .expirationDate(LocalDateTime.now().plusMonths(1))
                .paymentStatus("Pendiente")
                .description("Asignacion")
                .build();
    }

    private Account sharedAccount() {
        return Account.builder().idAccount(1).maxUsers(3).name("Shared").build();
    }

    @Test
    void findById_whenMissing_throwsException() {
        when(subscriptionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(99));
    }

    @Test
    void create_whenValid_createsActiveSubscription() {
        when(accountRepository.existsById(1)).thenReturn(true);
        when(appUserRepository.existsById(10)).thenReturn(true);
        when(subscriptionRepository.existsByIdAccountAndIdAppUserAndIsActiveTrue(1, 10)).thenReturn(false);
        when(accountRepository.findById(1)).thenReturn(Optional.of(sharedAccount()));
        when(subscriptionRepository.countByIdAccountAndIsActiveTrue(1)).thenReturn(0L);
        when(subscriptionRepository.save(any(AccountSubscription.class))).thenAnswer(invocation -> {
            AccountSubscription saved = invocation.getArgument(0);
            saved.setIdAccountDetail(100);
            return saved;
        });

        AccountSubscription result = service.create(validRequest());

        assertEquals(100, result.getIdAccountDetail());
        assertTrue(result.getIsActive());
        assertEquals("Pendiente", result.getPaymentStatus());
        assertNotNull(result.getStartDate());
        assertNotNull(result.getPaymentDate());
    }

    @Test
    void create_whenDuplicateAssignment_throwsException() {
        when(accountRepository.existsById(1)).thenReturn(true);
        when(appUserRepository.existsById(10)).thenReturn(true);
        when(subscriptionRepository.existsByIdAccountAndIdAppUserAndIsActiveTrue(1, 10)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(validRequest()));
        assertTrue(ex.getMessage().contains("already assigned"));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void create_whenAccountIsFull_throwsException() {
        when(accountRepository.existsById(1)).thenReturn(true);
        when(appUserRepository.existsById(10)).thenReturn(true);
        when(subscriptionRepository.existsByIdAccountAndIdAppUserAndIsActiveTrue(1, 10)).thenReturn(false);
        when(accountRepository.findById(1)).thenReturn(Optional.of(sharedAccount()));
        when(subscriptionRepository.countByIdAccountAndIsActiveTrue(1)).thenReturn(3L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(validRequest()));
        assertTrue(ex.getMessage().contains("maximum users"));
    }

    @Test
    void create_whenCompleteAccountAlreadyHasUser_throwsException() {
        Account completeAccount = Account.builder().idAccount(1).maxUsers(1).build();
        when(accountRepository.existsById(1)).thenReturn(true);
        when(appUserRepository.existsById(10)).thenReturn(true);
        when(subscriptionRepository.existsByIdAccountAndIdAppUserAndIsActiveTrue(1, 10)).thenReturn(false);
        when(accountRepository.findById(1)).thenReturn(Optional.of(completeAccount));
        when(subscriptionRepository.countByIdAccountAndIsActiveTrue(1)).thenReturn(1L);

        assertThrows(RuntimeException.class, () -> service.create(validRequest()));
    }

    @Test
    void create_whenInvalidPaymentStatus_throwsException() {
        when(accountRepository.existsById(1)).thenReturn(true);
        when(appUserRepository.existsById(10)).thenReturn(true);

        AccountSubscriptionRequestDto request = validRequest();
        request.setPaymentStatus("Otro");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(request));
        assertTrue(ex.getMessage().contains("Payment status"));
    }

    @Test
    void updatePaymentStatus_toPagado_updatesPaymentDate() {
        AccountSubscription subscription = AccountSubscription.builder()
                .idAccountDetail(5)
                .paymentStatus("Pendiente")
                .paymentDate(LocalDateTime.now().minusDays(5))
                .build();
        when(subscriptionRepository.findById(5)).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(AccountSubscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountSubscription result = service.updatePaymentStatus(5, UpdatePaymentStatusRequestDto.builder()
                .paymentStatus("pagado")
                .build());

        assertEquals("Pagado", result.getPaymentStatus());
        assertNotNull(result.getPaymentDate());
    }

    @Test
    void changeAccount_whenTargetHasSpace_movesSubscription() {
        AccountSubscription subscription = AccountSubscription.builder()
                .idAccountDetail(5)
                .idAccount(1)
                .idAppUser(10)
                .isActive(true)
                .build();
        when(subscriptionRepository.findById(5)).thenReturn(Optional.of(subscription));
        when(accountRepository.existsById(2)).thenReturn(true);
        when(subscriptionRepository.existsByIdAccountAndIdAppUserAndIsActiveTrue(2, 10)).thenReturn(false);
        when(accountRepository.findById(2)).thenReturn(Optional.of(Account.builder().idAccount(2).maxUsers(3).build()));
        when(subscriptionRepository.countByIdAccountAndIsActiveTrue(2)).thenReturn(1L);
        when(subscriptionRepository.save(any(AccountSubscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountSubscription result = service.changeAccount(5, ChangeAccountRequestDto.builder().idAccount(2).build());

        assertEquals(2, result.getIdAccount());
    }

    @Test
    void deactivate_setsIsActiveFalse() {
        AccountSubscription subscription = AccountSubscription.builder()
                .idAccountDetail(5)
                .isActive(true)
                .build();
        when(subscriptionRepository.findById(5)).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(AccountSubscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountSubscription result = service.deactivate(5);

        assertFalse(result.getIsActive());
    }

    @Test
    void renew_whenActive_deactivatesCurrentAndCreatesNew() {
        AccountSubscription current = AccountSubscription.builder()
                .idAccountDetail(5)
                .idAccount(1)
                .idAppUser(10)
                .isActive(true)
                .amountPaid(new BigDecimal("20.00"))
                .expirationDate(LocalDateTime.now().minusDays(1))
                .paymentStatus("Pagado")
                .build();

        when(subscriptionRepository.findById(5)).thenReturn(Optional.of(current));
        when(accountRepository.existsById(1)).thenReturn(true);
        when(subscriptionRepository.saveAndFlush(any(AccountSubscription.class))).thenAnswer(invocation -> {
            AccountSubscription saved = invocation.getArgument(0);
            assertFalse(saved.getIsActive());
            return saved;
        });
        when(subscriptionRepository.save(any(AccountSubscription.class))).thenAnswer(invocation -> {
            AccountSubscription saved = invocation.getArgument(0);
            saved.setIdAccountDetail(99);
            return saved;
        });

        RenewSubscriptionRequestDto request = RenewSubscriptionRequestDto.builder()
                .idAccount(1)
                .amountPaid(new BigDecimal("30.00"))
                .expirationDate(LocalDateTime.now().plusMonths(1))
                .paymentStatus("Pendiente")
                .description("Renovacion")
                .build();

        AccountSubscription result = service.renew(5, request);

        assertEquals(99, result.getIdAccountDetail());
        assertEquals(10, result.getIdAppUser());
        assertEquals(1, result.getIdAccount());
        assertTrue(result.getIsActive());
        assertEquals("Pendiente", result.getPaymentStatus());
        assertEquals(new BigDecimal("30.00"), result.getAmountPaid());
        verify(subscriptionRepository).saveAndFlush(current);
    }

    @Test
    void renew_whenInactive_throwsException() {
        when(subscriptionRepository.findById(5)).thenReturn(Optional.of(
                AccountSubscription.builder().idAccountDetail(5).isActive(false).build()
        ));

        RenewSubscriptionRequestDto request = RenewSubscriptionRequestDto.builder()
                .idAccount(1)
                .amountPaid(new BigDecimal("30.00"))
                .expirationDate(LocalDateTime.now().plusMonths(1))
                .paymentStatus("Pendiente")
                .build();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.renew(5, request));
        assertTrue(ex.getMessage().contains("Only an active subscription"));
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void countActiveUsers_returnsAvailableSlots() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(sharedAccount()));
        when(subscriptionRepository.countByIdAccountAndIsActiveTrue(1)).thenReturn(2L);

        CountActiveUsersResponseDto result = service.countActiveUsers(1);

        assertEquals(1, result.getIdAccount());
        assertEquals(2, result.getActiveUsersCount());
        assertEquals(3, result.getMaxUsers());
        assertEquals(1, result.getAvailableSlots());
    }

    @Test
    void findPendingPayments_returnsList() {
        when(subscriptionRepository.findByPaymentStatusIgnoreCaseAndIsActiveTrue("Pendiente"))
                .thenReturn(List.of(AccountSubscription.builder().idAccountDetail(1).paymentStatus("Pendiente").build()));

        assertEquals(1, service.findPendingPayments().size());
    }

    @Test
    void delete_removesSubscription() {
        when(subscriptionRepository.findById(5)).thenReturn(Optional.of(
                AccountSubscription.builder().idAccountDetail(5).build()
        ));

        service.delete(5);

        verify(subscriptionRepository).deleteById(5);
    }
}
