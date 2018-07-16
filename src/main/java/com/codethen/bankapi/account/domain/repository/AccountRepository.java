package com.codethen.bankapi.account.domain.repository;

import com.codethen.bankapi.account.domain.errors.AccountAlreadyExistsException;
import com.codethen.bankapi.account.domain.errors.AccountNotExistsException;
import com.codethen.bankapi.account.domain.model.Account;
import com.codethen.bankapi.account.domain.model.Amount;

public interface AccountRepository {

    /**
     * Creates a new account.
     * @throws AccountAlreadyExistsException if an account already exists with same username.
     */
    void create(Account account);

    /**
     * Returns the {@link Account} for the given username or null if it doesn't exist.
     */
    Account findByUsername(String username);

    /**
     * Updates the {@link Amount} in the {@link Account} of the given username.
     * @throws AccountNotExistsException if an {@link Account} doesn't exist for the given username.
     */
    void updateAccountAmount(String username, Amount amount);
}
