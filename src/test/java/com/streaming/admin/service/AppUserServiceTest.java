package com.streaming.admin.service;

import com.streaming.admin.dto.request.AppUserRequestDto;
import com.streaming.admin.entity.AppUser;
import com.streaming.admin.repository.AccountSubscriptionRepository;
import com.streaming.admin.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AccountSubscriptionRepository accountSubscriptionRepository;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    void findAll_returnsUsers() {
        when(appUserRepository.findAll()).thenReturn(List.of(
                AppUser.builder().idAppUser(1).name("Juan").lastName("Perez").phoneNumber("70000000").build()
        ));

        assertEquals(1, appUserService.findAll().size());
    }

    @Test
    void findById_whenMissing_throwsException() {
        when(appUserRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> appUserService.findById(99));
    }

    @Test
    void create_savesAppUser() {
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser saved = invocation.getArgument(0);
            saved.setIdAppUser(8);
            return saved;
        });

        AppUser result = appUserService.create(AppUserRequestDto.builder()
                .name("Ana")
                .lastName("Lopez")
                .nickName("ana")
                .ci("123")
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .phoneNumber("71111111")
                .build());

        assertEquals(8, result.getIdAppUser());
        assertEquals("Ana", result.getName());
        assertEquals("71111111", result.getPhoneNumber());
    }

    @Test
    void update_modifiesAppUser() {
        AppUser existing = AppUser.builder()
                .idAppUser(1)
                .name("Old")
                .lastName("Name")
                .phoneNumber("70000000")
                .build();
        when(appUserRepository.findById(1)).thenReturn(Optional.of(existing));
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AppUser result = appUserService.update(1, AppUserRequestDto.builder()
                .name("Nuevo")
                .lastName("Apellido")
                .nickName("nick")
                .ci("999")
                .phoneNumber("72222222")
                .build());

        assertEquals("Nuevo", result.getName());
        assertEquals("72222222", result.getPhoneNumber());
    }

    @Test
    void delete_whenLinkedToSubscription_throwsException() {
        when(appUserRepository.findById(1)).thenReturn(Optional.of(AppUser.builder().idAppUser(1).build()));
        when(accountSubscriptionRepository.existsByIdAppUser(1)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> appUserService.delete(1));
        verify(appUserRepository, never()).deleteById(any());
    }

    @Test
    void delete_whenNotLinked_deletes() {
        when(appUserRepository.findById(1)).thenReturn(Optional.of(AppUser.builder().idAppUser(1).build()));
        when(accountSubscriptionRepository.existsByIdAppUser(1)).thenReturn(false);

        appUserService.delete(1);

        verify(appUserRepository).deleteById(1);
    }
}
