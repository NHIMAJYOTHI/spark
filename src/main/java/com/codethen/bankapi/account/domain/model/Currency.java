package com.codethen.bankapi.account.domain.model;

public enum Currency {

    EURO_CENTS("EUR", 100), DOLLAR_CENTS("USD", 100);

    public final String code;
    /** Fraction of a whole unit. For example, an Euro is divided into 100 cents. */
    public final double division;

    Currency(String code, double division) {
        this.code = code;
        this.division = division;
    }
}
