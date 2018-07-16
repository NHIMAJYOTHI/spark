package com.codethen.bankapi.domain.model;

public class Account {

    private final String username;
    private Amount amount;


    public Account(String username, Currency currency) {
        this.username = username;
        this.amount = new Amount(currency);
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
}
