package com.codethen.bankapi.api.dto;

import com.codethen.bankapi.domain.model.Account;
import com.codethen.bankapi.domain.model.Currency;

public class AccountDTO {

    public String username;
    public String currency;
    public double amount;

    public static AccountDTO from(Account account) {

        final AccountDTO accountDTO = new AccountDTO();
        accountDTO.username = account.getUsername();
        final Currency currency = account.getAmount().getCurrency();
        accountDTO.currency = currency.code;
        accountDTO.amount = account.getAmount().getUnits() / currency.division;
        return accountDTO;
    }
}
