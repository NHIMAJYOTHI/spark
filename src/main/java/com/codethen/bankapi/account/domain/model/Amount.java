package com.codethen.bankapi.account.domain.model;

import com.codethen.bankapi.account.domain.errors.NotEnoughUnitsException;

import java.util.Objects;

public class Amount {

    private final Currency currency;
    private long units;


    public Amount(Currency currency, long units) {
        this.currency = currency;
        this.units = units;
    }


    public Currency getCurrency() {
        return currency;
    }

    public long getUnits() {
        return units;
    }


    public void addUnits(long units) {
        this.units += units;
    }

    public void removeUnits(long units) {
        if (this.units < units) {
            throw new NotEnoughUnitsException();
        }
        this.units -= units;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return units == amount.units &&
            currency == amount.currency;
    }

    @Override
    public int hashCode() {

        return Objects.hash(currency, units);
    }
}
