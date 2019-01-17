package com.vborodin.exchangerates.controller;


import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.repository.ExchangeRateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static io.github.benas.randombeans.api.EnhancedRandom.randomCollectionOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ExchangeRateControllerTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private ExchangeRateController exchangeRateController;

    @Autowired
    private MockMvc mvc;

    private ExchangeRate exchangeRate;
    private List<ExchangeRate> exchangeRates = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(exchangeRateController).build();

        exchangeRate = random(ExchangeRate.class);
        exchangeRates.add(exchangeRate);
        exchangeRates.addAll(randomCollectionOf(10, ExchangeRate.class));

        when(exchangeRateRepository.findByIdCurrencyIgnoreCase(exchangeRate.getId().getCurrency()))
                .thenReturn(Collections.singletonList((exchangeRate)));
        when(exchangeRateRepository.findByIdBankIgnoreCase(exchangeRate.getId().getBank()))
                .thenReturn(Collections.singletonList((exchangeRate)));
        when(exchangeRateRepository.findByIdBankIgnoreCaseAndIdCurrencyIgnoreCase(exchangeRate.getId().getBank(), exchangeRate.getId().getCurrency()))
                .thenReturn(Collections.singletonList((exchangeRate)));
        when(exchangeRateRepository.findAll())
                .thenReturn(exchangeRates);
    }

    @Test
    public void exchangeRatesWithoutParams() throws Exception {
        this.mvc.perform(get("/api/v1/exchangerates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(exchangeRates.size())));
    }

    @Test
    public void exchangeRatesWithCurrencyParam() throws Exception {
        this.mvc.perform(get("/api/v1/exchangerates")
                .param("currency", exchangeRate.getId().getCurrency()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].currency", is(exchangeRate.getId().getCurrency())))
                .andExpect(jsonPath("$.[0].bank", is(exchangeRate.getId().getBank())));

        this.mvc.perform(get("/api/v1/exchangerates")
                .param("currency", "NONEXISTENT"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void exchangeRatesWithBankParam() throws Exception {
        this.mvc.perform(get("/api/v1/exchangerates")
                .param("bank", exchangeRate.getId().getBank()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].currency", is(exchangeRate.getId().getCurrency())))
                .andExpect(jsonPath("$.[0].bank", is(exchangeRate.getId().getBank())));

        this.mvc.perform(get("/api/v1/exchangerates")
                .param("bank", "NONEXISTENT"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void exchangeRatesWithCurrencyParamAndBankParam() throws Exception {
        this.mvc.perform(get("/api/v1/exchangerates")
                .param("bank", exchangeRate.getId().getBank())
                .param("currency", exchangeRate.getId().getCurrency()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.[0].currency", is(exchangeRate.getId().getCurrency())))
                .andExpect(jsonPath("$.[0].bank", is(exchangeRate.getId().getBank())));

        this.mvc.perform(get("/api/v1/exchangerates")
                .param("currency", "NONEXISTENT")
                .param("bank", "NONEXISTENT"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}