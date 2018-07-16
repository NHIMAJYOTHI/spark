package com.codethen.bankapi.domain.service;

import com.codethen.bankapi.domain.errors.AccountNotExistsException;
import com.codethen.bankapi.domain.model.Account;
import com.codethen.bankapi.domain.model.Amount;
import com.codethen.bankapi.domain.model.Currency;
import com.codethen.bankapi.domain.repository.AccountRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;

public class AccountServiceShould {

    private AccountRepository accountRepositoryMock;
    private AccountService accountService;

    @Before
    public void setUp() {
        accountRepositoryMock = Mockito.mock(AccountRepository.class);
        accountService = new AccountService(accountRepositoryMock);
    }

    @Test
    public void return_existing_account() {

        final String username = "some-user";
        final Account existingAccount = new Account(username, Currency.EURO_CENTS);
        existingAccount.getAmount().addUnits(100);

        Mockito.when(accountRepositoryMock.findByUsername(Mockito.anyString()))
            .thenReturn(existingAccount);

        final Account accountFound = accountService.findByUsername(username);
        Assert.assertThat(accountFound.getUsername(), is(username));
        Assert.assertThat(accountFound.getAmount().getCurrency(), is(Currency.EURO_CENTS));
        Assert.assertThat(accountFound.getAmount().getUnits(), is(100L));
    }

    @Test
    public void return_null_when_getting_non_existing_account() {

        Mockito.when(accountRepositoryMock.findByUsername(Mockito.anyString()))
            .thenReturn(null);

        Assert.assertNull(accountService.findByUsername("some-user"));
    }
}