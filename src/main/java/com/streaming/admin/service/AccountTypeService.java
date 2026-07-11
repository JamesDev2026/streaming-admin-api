package com.streaming.admin.service;

import com.streaming.admin.dto.request.AccountTypeRequestDto;
import com.streaming.admin.entity.AccountType;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.AccountTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;
    private final AccountRepository accountRepository;

    public AccountTypeService(AccountTypeRepository accountTypeRepository, AccountRepository accountRepository) {
        this.accountTypeRepository = accountTypeRepository;
        this.accountRepository = accountRepository;
    }

    public List<AccountType> findAll() {
        return accountTypeRepository.findAll();
    }

    public AccountType findById(Integer id) {
        return accountTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AccountType not found"));
    }

    public AccountType create(AccountTypeRequestDto request) {
        AccountType accountType = AccountType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return accountTypeRepository.save(accountType);
    }

    public AccountType update(Integer id, AccountTypeRequestDto request) {
        AccountType accountType = findById(id);
        accountType.setName(request.getName());
        accountType.setDescription(request.getDescription());
        return accountTypeRepository.save(accountType);
    }

    public void delete(Integer id) {
        findById(id);

        if (accountRepository.existsByIdAccountType(id)) {
            throw new RuntimeException("Cannot delete account type because it is linked to one or more accounts");
        }

        accountTypeRepository.deleteById(id);
    }
}
