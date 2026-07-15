package com.streaming.admin.service;

import com.streaming.admin.dto.request.AccountRequestDto;
import com.streaming.admin.entity.Account;
import com.streaming.admin.repository.AccountRepository;
import com.streaming.admin.repository.AccountSubscriptionRepository;
import com.streaming.admin.repository.AccountTypeRepository;
import com.streaming.admin.repository.CategoryRepository;
import com.streaming.admin.repository.PanelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountSubscriptionRepository accountSubscriptionRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final CategoryRepository categoryRepository;
    private final PanelRepository panelRepository;

    public AccountService(
            AccountRepository accountRepository,
            AccountSubscriptionRepository accountSubscriptionRepository,
            AccountTypeRepository accountTypeRepository,
            CategoryRepository categoryRepository,
            PanelRepository panelRepository
    ) {
        this.accountRepository = accountRepository;
        this.accountSubscriptionRepository = accountSubscriptionRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.categoryRepository = categoryRepository;
        this.panelRepository = panelRepository;
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account create(AccountRequestDto request) {
        validateForeignKeys(request);

        Account account = Account.builder()
                .name(request.getName())
                .userName(request.getUserName())
                .password(request.getPassword())
                .description(request.getDescription())
                .purchaseDate(request.getPurchaseDate())
                .expirationDate(request.getExpirationDate())
                .price(request.getPrice())
                .maxUsers(request.getMaxUsers())
                .idAccountType(request.getIdAccountType())
                .idCategory(request.getIdCategory())
                .idPanel(request.getIdPanel())
                .build();

        return accountRepository.save(account);
    }

    public Account update(Integer id, AccountRequestDto request) {
        Account account = findById(id);
        validateForeignKeys(request);

        account.setName(request.getName());
        account.setUserName(request.getUserName());
        account.setPassword(request.getPassword());
        account.setDescription(request.getDescription());
        account.setPurchaseDate(request.getPurchaseDate());
        account.setExpirationDate(request.getExpirationDate());
        account.setPrice(request.getPrice());
        account.setMaxUsers(request.getMaxUsers());
        account.setIdAccountType(request.getIdAccountType());
        account.setIdCategory(request.getIdCategory());
        account.setIdPanel(request.getIdPanel());

        return accountRepository.save(account);
    }

    public void delete(Integer id) {
        findById(id);

        if (accountSubscriptionRepository.existsByIdAccount(id)) {
            throw new RuntimeException("Cannot delete account because it is linked to one or more subscriptions");
        }

        accountRepository.deleteById(id);
    }

    private void validateForeignKeys(AccountRequestDto request) {
        if (!accountTypeRepository.existsById(request.getIdAccountType())) {
            throw new RuntimeException("AccountType not found");
        }
        if (!categoryRepository.existsById(request.getIdCategory())) {
            throw new RuntimeException("Category not found");
        }
        if (!panelRepository.existsById(request.getIdPanel())) {
            throw new RuntimeException("Panel not found");
        }
    }
}
