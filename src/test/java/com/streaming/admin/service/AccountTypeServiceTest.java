package com.streaming.admin.service;

import com.streaming.admin.dto.request.AccountTypeRequestDto;
import com.streaming.admin.entity.AccountType;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.AccountTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountTypeServiceTest {

    @Mock
    private AccountTypeRepository accountTypeRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountTypeService accountTypeService;

    @Test
    void findAll_returnsTypes() {
        when(accountTypeRepository.findAll()).thenReturn(List.of(
                AccountType.builder().idAccountType(1).name("Compartida").build()
        ));

        assertEquals(1, accountTypeService.findAll().size());
    }

    @Test
    void findById_whenMissing_throwsException() {
        when(accountTypeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountTypeService.findById(99));
    }

    @Test
    void create_savesAccountType() {
        when(accountTypeRepository.save(any(AccountType.class))).thenAnswer(invocation -> {
            AccountType saved = invocation.getArgument(0);
            saved.setIdAccountType(2);
            return saved;
        });

        AccountType result = accountTypeService.create(AccountTypeRequestDto.builder()
                .name("Completa")
                .description("Un solo cliente")
                .build());

        assertEquals(2, result.getIdAccountType());
        assertEquals("Completa", result.getName());
    }

    @Test
    void update_modifiesAccountType() {
        AccountType existing = AccountType.builder().idAccountType(1).name("Old").build();
        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(existing));
        when(accountTypeRepository.save(any(AccountType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccountType result = accountTypeService.update(1, AccountTypeRequestDto.builder()
                .name("Completa")
                .description("Updated")
                .build());

        assertEquals("Completa", result.getName());
        assertEquals("Updated", result.getDescription());
    }

    @Test
    void delete_whenLinked_throwsException() {
        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(AccountType.builder().idAccountType(1).build()));
        when(accountRepository.existsByIdAccountType(1)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> accountTypeService.delete(1));
        verify(accountTypeRepository, never()).deleteById(any());
    }

    @Test
    void delete_whenNotLinked_deletes() {
        when(accountTypeRepository.findById(1)).thenReturn(Optional.of(AccountType.builder().idAccountType(1).build()));
        when(accountRepository.existsByIdAccountType(1)).thenReturn(false);

        accountTypeService.delete(1);

        verify(accountTypeRepository).deleteById(1);
    }
}
