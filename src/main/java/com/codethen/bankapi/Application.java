package com.codethen.bankapi;

import com.codethen.bankapi.api.AccountApi;
import com.codethen.bankapi.api.util.ContentType;
import com.codethen.bankapi.domain.model.Account;
import com.codethen.bankapi.domain.model.Amount;
import com.codethen.bankapi.domain.model.Currency;
import com.codethen.bankapi.domain.repository.AccountRepository;
import com.codethen.bankapi.domain.service.AccountService;
import com.codethen.bankapi.repository.InMemoryAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import spark.Filter;

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

        after((Filter) (req, res) -> {
            res.type(ContentType.APPLICATION_JSON);
        });

        path("/api/v1", () -> {
            AccountApi.init(mapper, accountService);
        });
    }
}