package com.vborodin.exchangerates.model;


import java.io.Serializable;
import java.math.BigDecimal;

public class BestRate implements Serializable {
    private String currency;
    private BigDecimal buyRate;
    private String buyBank;
    private BigDecimal sellRate;
    private String sellBank;

    public BestRate() {
    }

    public BestRate(String currency, BigDecimal buyRate, String buyBank, BigDecimal sellRate, String sellBank) {
        this.currency = currency;
        this.buyRate = buyRate;
        this.buyBank = buyBank;
        this.sellRate = sellRate;
        this.sellBank = sellBank;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public String getBuyBank() {
        return buyBank;
    }

    public void setBuyBank(String buyBank) {
        this.buyBank = buyBank;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }

    public String getSellBank() {
        return sellBank;
    }

    public void setSellBank(String sellBank) {
        this.sellBank = sellBank;
    }
}