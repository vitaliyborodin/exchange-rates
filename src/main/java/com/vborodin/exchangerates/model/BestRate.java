package com.vborodin.exchangerates.model;


import java.io.Serializable;
import java.math.BigDecimal;

public class BestRate implements Serializable {
    private Currency currency;
    private BigDecimal buyRate;
    private Bank buyBank;
    private BigDecimal sellRate;
    private Bank sellBank;

    public BestRate() {
    }

    public BestRate(Currency currency, BigDecimal buyRate, Bank buyBank, BigDecimal sellRate, Bank sellBank) {
        this.currency = currency;
        this.buyRate = buyRate;
        this.buyBank = buyBank;
        this.sellRate = sellRate;
        this.sellBank = sellBank;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public Bank getBuyBank() {
        return buyBank;
    }

    public void setBuyBank(Bank buyBank) {
        this.buyBank = buyBank;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public Bank getSellBank() {
        return sellBank;
    }

    public void setSellBank(Bank sellBank) {
        this.sellBank = sellBank;
    }
}