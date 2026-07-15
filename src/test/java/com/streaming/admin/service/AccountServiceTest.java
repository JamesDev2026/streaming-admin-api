package com.streaming.admin.service;

import com.streaming.admin.dto.request.AccountRequestDto;
import com.streaming.admin.entity.Account;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.AccountSubscriptionRepository;
import com.streaming.admin.repository.AccountTypeRepository;
import com.streaming.admin.repository.CategoryRepository;
import com.streaming.admin.repository.PanelRepository;
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
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountSubscriptionRepository accountSubscriptionRepository;

    @Mock
    private AccountTypeRepository accountTypeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PanelRepository panelRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountRequestDto validRequest() {
        return AccountRequestDto.builder()
                .name("Netflix Familia")
                .userName("user@mail.com")
                .password("secret")
                .description("Cuenta compartida")
                .purchaseDate(LocalDateTime.now())
                .expirationDate(LocalDateTime.now().plusMonths(1))
                .price(new BigDecimal("15.99"))
                .maxUsers(3)
                .idAccountType(1)
                .idCategory(1)
                .idPanel(1)
                .build();
    }

    @Test
    void findAll_returnsAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(
                Account.builder().idAccount(1).name("Netflix").build()
        ));

        assertEquals(1, accountService.findAll().size());
    }

    @Test
    void findById_whenMissing_throwsException() {
        when(accountRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.findById(99));
    }

    @Test
    void create_whenForeignKeysValid_savesAccount() {
        AccountRequestDto request = validRequest();
        when(accountTypeRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(panelRepository.existsById(1)).thenReturn(true);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account saved = invocation.getArgument(0);
            saved.setIdAccount(20);
            return saved;
        });

        Account result = accountService.create(request);

        assertEquals(20, result.getIdAccount());
        assertEquals("Netflix Familia", result.getName());
        assertEquals(3, result.getMaxUsers());
    }

    @Test
    void create_whenCategoryMissing_throwsException() {
        when(accountTypeRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> accountService.create(validRequest()));
        assertEquals("Category not found", ex.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void create_whenPanelMissing_throwsException() {
        when(accountTypeRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(panelRepository.existsById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> accountService.create(validRequest()));
        assertEquals("Panel not found", ex.getMessage());
    }

    @Test
    void update_modifiesAccount() {
        Account existing = Account.builder()
                .idAccount(1)
                .name("Old")
                .userName("old@mail.com")
                .password("old")
                .price(BigDecimal.ONE)
                .maxUsers(1)
                .idAccountType(1)
                .idCategory(1)
                .idPanel(1)
                .build();

        when(accountRepository.findById(1)).thenReturn(Optional.of(existing));
        when(accountTypeRepository.existsById(1)).thenReturn(true);
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(panelRepository.existsById(1)).thenReturn(true);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account result = accountService.update(1, validRequest());

        assertEquals("Netflix Familia", result.getName());
        assertEquals("user@mail.com", result.getUserName());
    }

    @Test
    void delete_whenLinkedToSubscription_throwsException() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(Account.builder().idAccount(1).build()));
        when(accountSubscriptionRepository.existsByIdAccount(1)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> accountService.delete(1));
        verify(accountRepository, never()).deleteById(any());
    }

    @Test
    void delete_whenNotLinked_deletes() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(Account.builder().idAccount(1).build()));
        when(accountSubscriptionRepository.existsByIdAccount(1)).thenReturn(false);

        accountService.delete(1);

        verify(accountRepository).deleteById(1);
    }
}
