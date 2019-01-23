package com.vborodin.exchangerates.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@Embeddable
@JsonPropertyOrder({"currency","bank"})
@XmlAccessorType(XmlAccessType.FIELD)
public class ExchangeRateId implements Serializable {
    @XmlElement
    private String currency;
    @XmlElement
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
