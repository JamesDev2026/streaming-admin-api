package com.streaming.admin.repository;

import com.streaming.admin.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    boolean existsByIdCategory(Integer idCategory);

    boolean existsByIdAccountType(Integer idAccountType);

    boolean existsByIdPanel(Integer idPanel);
}