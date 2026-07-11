package com.streaming.admin.service;

import com.streaming.admin.dto.request.AppUserRequestDto;
import com.streaming.admin.entity.AppUser;
import com.streaming.admin.repository.AccountSubscriptionRepository;
import com.streaming.admin.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AccountSubscriptionRepository accountSubscriptionRepository;

    public AppUserService(
            AppUserRepository appUserRepository,
            AccountSubscriptionRepository accountSubscriptionRepository
    ) {
        this.appUserRepository = appUserRepository;
        this.accountSubscriptionRepository = accountSubscriptionRepository;
    }

    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    public AppUser findById(Integer id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AppUser not found"));
    }

    public AppUser create(AppUserRequestDto request) {
        AppUser appUser = AppUser.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .nickName(request.getNickName())
                .ci(request.getCi())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .build();

        return appUserRepository.save(appUser);
    }

    public AppUser update(Integer id, AppUserRequestDto request) {
        AppUser appUser = findById(id);
        appUser.setName(request.getName());
        appUser.setLastName(request.getLastName());
        appUser.setNickName(request.getNickName());
        appUser.setCi(request.getCi());
        appUser.setDateOfBirth(request.getDateOfBirth());
        appUser.setPhoneNumber(request.getPhoneNumber());
        return appUserRepository.save(appUser);
    }

    public void delete(Integer id) {
        findById(id);

        if (accountSubscriptionRepository.existsByIdAppUser(id)) {
            throw new RuntimeException("Cannot delete app user because it is linked to one or more subscriptions");
        }

        appUserRepository.deleteById(id);
    }
}
