package com.codethen.bankapi.account.api;

import com.codethen.bankapi.account.api.dto.AccountDTO;
import com.codethen.bankapi.account.api.dto.TransferDTO;
import com.codethen.bankapi.account.domain.model.Account;
import com.codethen.bankapi.account.domain.service.AccountService;
import com.codethen.bankapi.common.api.dto.MessageDTO;
import com.codethen.bankapi.common.api.security.AuthUser;
import com.codethen.bankapi.common.api.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.get;
import static spark.Spark.post;

public class AccountApi {

    public static void setupEndpoints(
        ObjectMapper mapper,
        AccountService accountService) {

        get("/balance", (req, res) -> {

            final AuthUser authUser = JwtUtil.getAuthUser(req);
            final Account account = accountService.findByUsername(authUser.username);

            return mapper.writeValueAsString(AccountDTO.from(account));
        });

        post("/transfer", (req, res) -> {

            final AuthUser authUser = JwtUtil.getAuthUser(req);
            final TransferDTO transfer = mapper.readValue(req.body(), TransferDTO.class);
            accountService.transferMoney(authUser.username, transfer.toUser, transfer.units);

            return mapper.writeValueAsString(new MessageDTO("ok"));
        });
    }
}
