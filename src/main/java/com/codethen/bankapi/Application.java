package com.codethen.bankapi;

import com.codethen.bankapi.account.api.AccountApi;
import com.codethen.bankapi.common.api.ContentType;
import com.codethen.bankapi.account.domain.errors.AccountAlreadyExistsException;
import com.codethen.bankapi.account.domain.errors.AccountNotExistsException;
import com.codethen.bankapi.account.domain.errors.CurrenciesDontMatchException;
import com.codethen.bankapi.account.domain.errors.NotEnoughUnitsException;
import com.codethen.bankapi.account.domain.model.Account;
import com.codethen.bankapi.account.domain.model.Amount;
import com.codethen.bankapi.account.domain.model.Currency;
import com.codethen.bankapi.account.domain.repository.AccountRepository;
import com.codethen.bankapi.account.domain.service.AccountService;
import com.codethen.bankapi.account.repository.InMemoryAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.eclipse.jetty.http.HttpStatus;

import static spark.Spark.*;

public class Application {
    
    public static void main(String[] args) {

        final AccountRepository accountRepository = new InMemoryAccountRepository();
        final AccountService accountService = new AccountService(accountRepository);

        // TODO: test accounts
        accountService.create(new Account("john", new Amount(Currency.DOLLAR_CENTS, 10000)));
        accountService.create(new Account("mary", new Amount(Currency.DOLLAR_CENTS, 20050)));
        accountService.create(new Account("pepe", new Amount(Currency.EURO_CENTS, 3000)));

        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        setupResponsesContentType();
        addExceptionHandlers();
        setupEndpoints(mapper, accountService);
    }

    private static void addExceptionHandlers() {

        exception(AccountNotExistsException.class, (ex, req, res) -> {
            res.status(HttpStatus.NOT_FOUND_404);
            res.body("Account not found");
        });

        exception(AccountAlreadyExistsException.class, (ex, req, res) -> {
            res.status(HttpStatus.PRECONDITION_FAILED_412);
            res.body("Account already exists");
        });

        exception(CurrenciesDontMatchException.class, (ex, req, res) -> {
            res.status(HttpStatus.PRECONDITION_FAILED_412);
            res.body("Currencies don't match");
        });

        exception(NotEnoughUnitsException.class, (ex, req, res) -> {
            res.status(HttpStatus.PRECONDITION_FAILED_412);
            res.body("Not enough funds");
        });
    }

    private static void setupResponsesContentType() {
        after("/api/*", (req, res) -> {
            res.type(ContentType.APPLICATION_JSON);
        });
    }

    private static void setupEndpoints(ObjectMapper mapper, AccountService accountService) {
        path("/api/v1", () -> {
            AccountApi.setupEndpoints(mapper, accountService);
        });
    }
}