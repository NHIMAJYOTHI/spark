package com.codethen.bankapi.domain.service;

import com.codethen.bankapi.domain.model.Account;
import com.codethen.bankapi.domain.repository.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Returns the {@link Account} for the given username or null if it doesn't exist.
     */
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
