package com.codethen.bankapi.repository;

import com.codethen.bankapi.domain.errors.AccountAlreadyExistsException;
import com.codethen.bankapi.domain.errors.AccountNotExistsException;
import com.codethen.bankapi.domain.model.Account;
import com.codethen.bankapi.domain.model.Amount;
import com.codethen.bankapi.domain.repository.AccountRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAccountRepository implements AccountRepository {

    private Map<String, Account> accounts = new HashMap<>();

    @Override
    public void create(Account account) {
        if (accounts.containsKey(account.getUsername())) {
            throw new AccountAlreadyExistsException();
        }
        accounts.put(account.getUsername(), account);
    }

    @Override
    public Account findByUsername(String username) {
        return accounts.get(username);
    }

    @Override
    public void updateAccountAmount(String username, Amount amount) {
        final Account account = findByUsername(username);
        if (account == null) {
            throw new AccountNotExistsException();
        }
        account.setAmount(amount);
    }
}
