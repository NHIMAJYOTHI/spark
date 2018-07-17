package com.codethen.bankapi.account.domain.service;

import com.codethen.bankapi.account.domain.errors.AccountNotExistsException;
import com.codethen.bankapi.account.domain.errors.CurrenciesDontMatchException;
import com.codethen.bankapi.account.domain.errors.NotEnoughUnitsException;
import com.codethen.bankapi.account.domain.model.Account;
import com.codethen.bankapi.account.domain.model.Amount;
import com.codethen.bankapi.account.domain.model.Currency;
import com.codethen.bankapi.account.domain.repository.AccountRepository;
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

        Mockito.when(accountRepositoryMock.findByUsername(Mockito.anyString()))
            .thenReturn(new Account(username, new Amount(Currency.EURO_CENTS, 100)));

        Assert.assertThat(accountService.findByUsername(username),
            is(new Account(username, new Amount(Currency.EURO_CENTS, 100))));
    }

    @Test(expected = AccountNotExistsException.class)
    public void fail_when_getting_non_existing_account() {

        Mockito.when(accountRepositoryMock.findByUsername(Mockito.anyString()))
            .thenReturn(null);

        accountService.findByUsername("some-user");
    }

    @Test(expected = IllegalArgumentException.class)
    public void fail_if_transferring_between_same_user() {

        accountService.transferMoney("same", "same", 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fail_if_units_are_not_positive() {

        accountService.transferMoney("from", "to", 0);
    }

    @Test(expected = AccountNotExistsException.class)
    public void fail_if_transferring_between_non_existing_accounts() {

        Mockito.when(accountRepositoryMock.findByUsername(Mockito.anyString()))
            .thenReturn(null);

        accountService.transferMoney("from", "to", 100);
    }

    @Test(expected = AccountNotExistsException.class)
    public void fail_if_transferring_from_non_existing_account() {

        Mockito.when(accountRepositoryMock.findByUsername("from"))
            .thenReturn(null);

        Mockito.when(accountRepositoryMock.findByUsername("to"))
            .thenReturn(new Account("to", new Amount(Currency.EURO_CENTS, 100)));

        accountService.transferMoney("from", "to", 100);
    }

    @Test(expected = AccountNotExistsException.class)
    public void fail_if_transferring_to_non_existing_account() {

        Mockito.when(accountRepositoryMock.findByUsername("from"))
            .thenReturn(new Account("from", new Amount(Currency.EURO_CENTS, 100)));

        Mockito.when(accountRepositoryMock.findByUsername("to"))
            .thenReturn(null);

        accountService.transferMoney("from", "to", 100);
    }

    @Test(expected = NotEnoughUnitsException.class)
    public void fail_if_transferring_from_account_without_enough_funds() {

        Mockito.when(accountRepositoryMock.findByUsername("from"))
            .thenReturn(new Account("from", new Amount(Currency.EURO_CENTS, 100)));

        Mockito.when(accountRepositoryMock.findByUsername("to"))
            .thenReturn(new Account("to", new Amount(Currency.EURO_CENTS, 100)));

        accountService.transferMoney("from", "to", 150);
    }

    @Test(expected = CurrenciesDontMatchException.class)
    public void fail_if_transferring_between_different_currenceis() {

        Mockito.when(accountRepositoryMock.findByUsername("from"))
            .thenReturn(new Account("from", new Amount(Currency.EURO_CENTS, 100)));

        Mockito.when(accountRepositoryMock.findByUsername("to"))
            .thenReturn(new Account("to", new Amount(Currency.DOLLAR_CENTS, 100)));

        accountService.transferMoney("from", "to", 100);
    }

    @Test
    public void transfer_money_between_accounts() {

        Mockito.when(accountRepositoryMock.findByUsername("from"))
            .thenReturn(new Account("from", new Amount(Currency.EURO_CENTS, 100)));

        Mockito.when(accountRepositoryMock.findByUsername("to"))
            .thenReturn(new Account("to", new Amount(Currency.EURO_CENTS, 100)));

        accountService.transferMoney("from", "to", 25);

        Mockito.verify(accountRepositoryMock).updateAccountAmount("from", new Amount(Currency.EURO_CENTS, 75));
        Mockito.verify(accountRepositoryMock).updateAccountAmount("to", new Amount(Currency.EURO_CENTS, 125));
    }
}