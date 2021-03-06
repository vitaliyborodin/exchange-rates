package com.vborodin.exchangerates.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@JsonPropertyOrder({"id", "buy", "sell"})
@EntityListeners(AuditingEntityListener.class)
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

    @Column
    @CreatedDate
    private LocalDateTime createdDate;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedDate;

    public ExchangeRate() {
    }

    public ExchangeRate(ExchangeRateId id, BigDecimal buy, BigDecimal sell) {
        this.id = id;
        this.buy = buy;
        this.sell = sell;
    }

    public ExchangeRate(Currency currency, BigDecimal buy, BigDecimal sell) {
        this.id = new ExchangeRateId();
        this.id.setCurrency(currency);
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

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
