package com.vborodin.exchangerates.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class ExchangeRate implements Serializable {

    @EmbeddedId
    @JsonUnwrapped
    private ExchangeRateId id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(precision = 20, scale = 2)
    private BigDecimal buy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(precision = 20, scale = 2)
    private BigDecimal sell;

    public ExchangeRate() {
    }

    public ExchangeRate(ExchangeRateId id, BigDecimal buy, BigDecimal sell) {
        this.id = id;
        this.buy = buy;
        this.sell = sell;
    }

    public ExchangeRateId getId() {
        return id;
    }

    public void setId(ExchangeRateId id) {
        this.id = id;
    }

    public BigDecimal getBuy() {
        return buy;
    }

    public void setBuy(BigDecimal buy) {
        this.buy = buy;
    }

    public BigDecimal getSell() {
        return sell;
    }

    public void setSell(BigDecimal sell) {
        this.sell = sell;
    }

}
