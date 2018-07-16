package com.codethen.bankapi.account.api;

import com.codethen.bankapi.account.api.dto.AccountDTO;
import com.codethen.bankapi.common.api.dto.MessageDTO;
import com.codethen.bankapi.account.api.dto.TransferDTO;
import com.codethen.bankapi.account.domain.model.Account;
import com.codethen.bankapi.account.domain.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.get;
import static spark.Spark.post;

public class AccountApi {

    public static void setupEndpoints(
        ObjectMapper mapper,
        AccountService accountService) {

        get("/balance", (req, res) -> {

            // TODO: get user from req auth
            final String username = req.queryParams("user");

            final Account account = accountService.findByUsername(username);

            return mapper.writeValueAsString(AccountDTO.from(account));
        });

        post("/transfer", (req, res) -> {

            // TODO: get user from req auth
            final String username = req.queryParams("user");

            final TransferDTO transfer = mapper.readValue(req.body(), TransferDTO.class);

            accountService.transferMoney(username, transfer.toUser, transfer.units);

            return mapper.writeValueAsString(new MessageDTO("ok"));
        });
    }
}
