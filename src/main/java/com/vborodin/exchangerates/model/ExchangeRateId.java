package com.vborodin.exchangerates.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@Embeddable
@JsonPropertyOrder({"currency","bank"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ExchangeRateId implements Serializable {
    @XmlElement
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @XmlElement
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Bank.class)
    private Bank bank;

    ExchangeRateId() {
    }

    public ExchangeRateId(Currency currency, Bank bank) {
        this.currency = currency;
        this.bank = bank;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
