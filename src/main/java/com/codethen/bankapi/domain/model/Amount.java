package com.codethen.bankapi.domain.model;

import com.codethen.bankapi.domain.errors.NotEnoughUnitsException;

public class Amount {

    private final Currency currency;
    private long units;


    public Amount(Currency currency) {
        this.currency = currency;
        this.units = 0;
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
}
