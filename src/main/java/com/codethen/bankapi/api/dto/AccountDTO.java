package com.codethen.bankapi.api.dto;

import com.codethen.bankapi.domain.model.Account;
import com.codethen.bankapi.domain.model.Amount;
import com.codethen.bankapi.domain.model.Currency;

public class AccountDTO {

    public String username;
    /** Currency code like "EUR" or "USD" */
    public String currency;
    /** Available amount in units (e.g. for EUR this will be cents) */
    public long units;
    /** Available amount in "whole coins" (e.g. Euros). This value may have precision issues. */
    public double amount;

    public static AccountDTO from(Account account) {

        final Amount amount = account.getAmount();
        final Currency currency = amount.getCurrency();

        final AccountDTO accountDTO = new AccountDTO();
        accountDTO.username = account.getUsername();
        accountDTO.currency = currency.code;
        accountDTO.units = amount.getUnits();
        accountDTO.amount = amount.getUnits() / currency.division;
        return accountDTO;
    }
}
