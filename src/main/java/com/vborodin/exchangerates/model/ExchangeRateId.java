package com.vborodin.exchangerates.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ExchangeRateId implements Serializable {
    private String currency;
    private String bank;

    public ExchangeRateId() {
    }

    public ExchangeRateId(String currency, String bank) {
        this.currency = currency;
        this.bank = bank;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
