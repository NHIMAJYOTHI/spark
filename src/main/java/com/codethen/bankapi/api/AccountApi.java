package com.codethen.bankapi.api;

import com.codethen.bankapi.api.dto.AccountDTO;
import com.codethen.bankapi.api.util.ContentType;
import com.codethen.bankapi.api.util.HttpStatus;
import com.codethen.bankapi.domain.errors.AccountNotExistsException;
import com.codethen.bankapi.domain.model.Account;
import com.codethen.bankapi.domain.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.get;
import static spark.Spark.halt;

public class AccountApi {

    public static void init(
        ObjectMapper mapper,
        AccountService accountService) {

        get("/balance", (req, res) -> {

            try {
                // TODO: get user from req auth
                final String username = req.queryParams("username");
                final Account account = accountService.findByUsername(username);

                return mapper.writeValueAsString(AccountDTO.from(account));

            } catch (AccountNotExistsException e) {
                return halt(HttpStatus.NOT_FOUND, "No account found");
            }
        });
    }
}
