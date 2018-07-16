package com.codethen.bankapi;

import com.codethen.bankapi.account.api.AccountApi;
import com.codethen.bankapi.account.domain.errors.AccountAlreadyExistsException;
import com.codethen.bankapi.account.domain.errors.AccountNotExistsException;
import com.codethen.bankapi.account.domain.errors.CurrenciesDontMatchException;
import com.codethen.bankapi.account.domain.errors.NotEnoughUnitsException;
import com.codethen.bankapi.account.domain.model.Account;
import com.codethen.bankapi.account.domain.model.Amount;
import com.codethen.bankapi.account.domain.model.Currency;
import com.codethen.bankapi.account.domain.service.AccountService;
import com.codethen.bankapi.account.repository.InMemoryAccountRepository;
import com.codethen.bankapi.common.api.content.ContentType;
import com.codethen.bankapi.user.api.UserApi;
import com.codethen.bankapi.user.domain.errors.UserAlreadyExistsException;
import com.codethen.bankapi.user.domain.errors.UserNotExistsException;
import com.codethen.bankapi.user.domain.model.User;
import com.codethen.bankapi.user.repository.InMemoryUserRepository;
import com.codethen.bankapi.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.eclipse.jetty.http.HttpStatus;
import spark.Response;

import static spark.Spark.*;

public class Application {
    
    public static void main(String[] args) {

        final AccountService accountService = new AccountService(new InMemoryAccountRepository());
        final UserService userService = new UserService(new InMemoryUserRepository());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        setupResponsesContentType();
        addExceptionHandlers();
        setupEndpoints(mapper, accountService, userService);

        createDummyData(accountService, userService); // TODO: remove
    }

    private static void setupEndpoints(
        ObjectMapper mapper,
        AccountService accountService,
        UserService userService) {

        path("/api/v1", () -> {

            path("/account", () -> {
                AccountApi.setupEndpoints(mapper, accountService);
            });

            path("/user", () -> {
                UserApi.setupEndpoints(mapper, userService);
            });
        });
    }

    private static void setupResponsesContentType() {
        after("/api/*", (req, res) -> {
            res.type(ContentType.APPLICATION_JSON);
        });
    }

    // TODO: move handlers to each corresponding api
    private static void addExceptionHandlers() {

        exception(AccountNotExistsException.class, (ex, req, res) -> {
            setupResponse(res, HttpStatus.NOT_FOUND_404, "Account not found");
        });

        exception(AccountAlreadyExistsException.class, (ex, req, res) -> {
            setupResponse(res, HttpStatus.PRECONDITION_FAILED_412, "Account already exists");
        });

        exception(UserNotExistsException.class, (ex, req, res) -> {
            setupResponse(res, HttpStatus.NOT_FOUND_404, "User not found");
        });

        exception(UserAlreadyExistsException.class, (ex, req, res) -> {
            setupResponse(res, HttpStatus.PRECONDITION_FAILED_412, "User already exists");
        });

        exception(CurrenciesDontMatchException.class, (ex, req, res) -> {
            setupResponse(res, HttpStatus.PRECONDITION_FAILED_412, "Currencies don't match");
        });

        exception(NotEnoughUnitsException.class, (ex, req, res) -> {
            setupResponse(res, HttpStatus.PRECONDITION_FAILED_412, "Not enough funds");
        });
    }

    private static void setupResponse(Response res, int status, String message) {
        res.status(status);
        res.body(message);
    }

    private static void createDummyData(AccountService accountService, UserService userService) {
        userService.create(new User("john", "john-password"));
        userService.create(new User("mary", "mary-password"));
        userService.create(new User("pepe", "pepe-password"));
        accountService.create(new Account("john", new Amount(Currency.DOLLAR_CENTS, 10000)));
        accountService.create(new Account("mary", new Amount(Currency.DOLLAR_CENTS, 20050)));
        accountService.create(new Account("pepe", new Amount(Currency.EURO_CENTS, 3000)));
    }
}