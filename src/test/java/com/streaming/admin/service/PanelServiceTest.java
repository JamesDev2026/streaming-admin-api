package com.streaming.admin.service;

import com.streaming.admin.dto.request.PanelRequestDto;
import com.streaming.admin.entity.Panel;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.PanelRepository;
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
class PanelServiceTest {

    @Mock
    private PanelRepository panelRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PanelService panelService;

    @Test
    void findAll_returnsPanels() {
        when(panelRepository.findAll()).thenReturn(List.of(
                Panel.builder().idPanel(1).name("Main").url("https://panel.test").build()
        ));

        assertEquals(1, panelService.findAll().size());
    }

    @Test
    void findById_whenMissing_throwsException() {
        when(panelRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> panelService.findById(99));
    }

    @Test
    void create_savesPanel() {
        when(panelRepository.save(any(Panel.class))).thenAnswer(invocation -> {
            Panel saved = invocation.getArgument(0);
            saved.setIdPanel(5);
            return saved;
        });

        Panel result = panelService.create(PanelRequestDto.builder()
                .name("Panel A")
                .url("https://example.com/panel")
                .description("Desc")
                .build());

        assertEquals(5, result.getIdPanel());
        assertEquals("https://example.com/panel", result.getUrl());
    }

    @Test
    void update_modifiesPanel() {
        Panel existing = Panel.builder().idPanel(1).name("Old").url("old").build();
        when(panelRepository.findById(1)).thenReturn(Optional.of(existing));
        when(panelRepository.save(any(Panel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Panel result = panelService.update(1, PanelRequestDto.builder()
                .name("New")
                .url("https://new.com")
                .description("Updated")
                .build());

        assertEquals("New", result.getName());
        assertEquals("https://new.com", result.getUrl());
    }

    @Test
    void delete_whenLinked_throwsException() {
        when(panelRepository.findById(1)).thenReturn(Optional.of(Panel.builder().idPanel(1).build()));
        when(accountRepository.existsByIdPanel(1)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> panelService.delete(1));
        verify(panelRepository, never()).deleteById(any());
    }

    @Test
    void delete_whenNotLinked_deletes() {
        when(panelRepository.findById(1)).thenReturn(Optional.of(Panel.builder().idPanel(1).build()));
        when(accountRepository.existsByIdPanel(1)).thenReturn(false);

        panelService.delete(1);

        verify(panelRepository).deleteById(1);
    }
}
