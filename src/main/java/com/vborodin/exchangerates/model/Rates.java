package com.vborodin.exchangerates.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "rates")
public class Rates implements Serializable {

    @JacksonXmlProperty(localName = "exchangeRate")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ExchangeRate> ratesList = new ArrayList<>();

    public Rates() {
    }

    public Rates(List<ExchangeRate> ratesList) {
        this.ratesList = ratesList;
    }

    public List<ExchangeRate> getRates() {
        return ratesList;
    }
}
