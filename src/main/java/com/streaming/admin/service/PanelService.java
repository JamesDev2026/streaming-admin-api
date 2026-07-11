package com.streaming.admin.service;

import com.streaming.admin.dto.request.PanelRequestDto;
import com.streaming.admin.entity.Panel;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.PanelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PanelService {

    private final PanelRepository panelRepository;
    private final AccountRepository accountRepository;

    public PanelService(PanelRepository panelRepository, AccountRepository accountRepository) {
        this.panelRepository = panelRepository;
        this.accountRepository = accountRepository;
    }

    public List<Panel> findAll() {
        return panelRepository.findAll();
    }

    public Panel findById(Integer id) {
        return panelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Panel not found"));
    }

    public Panel create(PanelRequestDto request) {
        Panel panel = Panel.builder()
                .name(request.getName())
                .url(request.getUrl())
                .description(request.getDescription())
                .build();

        return panelRepository.save(panel);
    }

    public Panel update(Integer id, PanelRequestDto request) {
        Panel panel = findById(id);
        panel.setName(request.getName());
        panel.setUrl(request.getUrl());
        panel.setDescription(request.getDescription());
        return panelRepository.save(panel);
    }

    public void delete(Integer id) {
        findById(id);

        if (accountRepository.existsByIdPanel(id)) {
            throw new RuntimeException("Cannot delete panel because it is linked to one or more accounts");
        }

        panelRepository.deleteById(id);
    }
}
