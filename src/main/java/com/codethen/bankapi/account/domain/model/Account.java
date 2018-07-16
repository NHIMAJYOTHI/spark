package com.codethen.bankapi.account.domain.model;

import java.util.Objects;

public class Account {

    private final String username;
    private Amount amount;


    public Account(String username, Amount initialAmount) {
        this.username = username;
        this.amount = initialAmount;
    }

    public Account(String username, Currency currency) {
        this(username, new Amount(currency, 0));
    }


    public String getUsername() {
        return username;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(username, account.username) &&
            Objects.equals(amount, account.amount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, amount);
    }
}
