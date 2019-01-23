package com.vborodin.exchangerates.controller;


import com.vborodin.exchangerates.ExchangeRatesApplication;
import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.repository.ExchangeRateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static io.github.benas.randombeans.api.EnhancedRandom.random;
import static io.github.benas.randombeans.api.EnhancedRandom.randomCollectionOf;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ExchangeRatesApplication.class})
public class ExchangeRateControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private MockMvc mvc;

    private ExchangeRate exchangeRate;
    private static List<ExchangeRate> exchangeRates = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();

        exchangeRate = random(ExchangeRate.class);
        exchangeRates.add(exchangeRate);
        exchangeRates.addAll(randomCollectionOf(10, ExchangeRate.class));
        exchangeRates.forEach(exchangeRateRepository::save);
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
    }
    
    @Test
    public void exchangeRatesWithNonexistentCurrencyParam() throws Exception {
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
    }
    
    @Test
    public void exchangeRatesWithNonexistentBankParam() throws Exception {
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
    }
    
    @Test
    public void exchangeRatesWithNonexistentCurrencyParamAndNonexistentBankParam() throws Exception {
        this.mvc.perform(get("/api/v1/exchangerates")
                .param("currency", "NONEXISTENT")
                .param("bank", "NONEXISTENT"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    
    @Test
    public void exchangeRatesWithNonexistentCurrencyParamAndBankParam() throws Exception {
        this.mvc.perform(get("/api/v1/exchangerates")
                .param("currency", "NONEXISTENT")
                .param("bank", exchangeRate.getId().getBank()))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    
    @Test
    public void exchangeRatesWithCurrencyParamAndNonexistentBankParam() throws Exception {
        this.mvc.perform(get("/api/v1/exchangerates")
                .param("currency", exchangeRate.getId().getCurrency())
                .param("bank", "NONEXISTENT"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
    
    @Test
    public void exchangeRatesByCurrencyIgnoreCaseForBuy() throws Exception {
        this.mvc.perform(get("/api/v1/exchangerates/{currency}/buy", exchangeRate.getId().getCurrency()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(1)))
        .andExpect(jsonPath("$.[0].currency", is(exchangeRate.getId().getCurrency())))
        .andExpect(jsonPath("$.[0].bank", is(exchangeRate.getId().getBank())));
    	
        this.mvc.perform(get("/api/v1/exchangerates/{currency}/buy", exchangeRate.getId().getCurrency().toLowerCase()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(1)))
        .andExpect(jsonPath("$.[0].currency", is(exchangeRate.getId().getCurrency())))
        .andExpect(jsonPath("$.[0].bank", is(exchangeRate.getId().getBank())));
        
        this.mvc.perform(get("/api/v1/exchangerates/{currency}/buy", exchangeRate.getId().getCurrency().toUpperCase()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(1)))
        .andExpect(jsonPath("$.[0].currency", is(exchangeRate.getId().getCurrency())))
        .andExpect(jsonPath("$.[0].bank", is(exchangeRate.getId().getBank())));
    }
    
    @Test
    public void uploadXMLFile() throws Exception {
        MockMultipartFile xmlFile = new MockMultipartFile("file", "test.xml", "application/xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><rates/>".getBytes());

        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                .file(xmlFile))
                .andExpect(status().isOk());
    }
        
    @Test
    public void uploadBadXMLFile() throws Exception {
        MockMultipartFile badXmlFile = new MockMultipartFile("file", "test.xml", "application/xml", "<?xml".getBytes());
        
        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
        		.file(badXmlFile))
        		.andExpect(status().isBadRequest())
        		.andExpect(jsonPath("$.message", is("XML processing error")));
    }
    
    @Test
    public void uploadJSONFile() throws Exception {
        MockMultipartFile jsonFile = new MockMultipartFile("file", "test.json", "application/json", "[]".getBytes());

        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                .file(jsonFile))
                .andExpect(status().isOk());
    }
    
    @Test
    public void uploadBadJSONFile() throws Exception {
        MockMultipartFile badJsonFile = new MockMultipartFile("file", "test.json", "application/json", "[".getBytes());
                
        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
        		.file(badJsonFile))
        		.andExpect(status().isBadRequest())
        		.andExpect(jsonPath("$.message", is("JSON processing error")));
    }
    
    @Test
    public void uploadCSVFile() throws Exception {
        MockMultipartFile csvFile = new MockMultipartFile("file", "test.csv", "plain/text", "Currency,Buy,Sell".getBytes());

        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                .file(csvFile))
                .andExpect(status().isOk());
    }
    
    @Test
    public void uploadBadCSVFile() throws Exception {
        MockMultipartFile badCsvFile = new MockMultipartFile("file", "test.csv", "plain/text", ",\n\n,\n,\n,,\t,,,\n,,,,\n/\n///|,,,,,".getBytes());

        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
        		.file(badCsvFile))
        		.andExpect(status().isBadRequest())
        		.andExpect(jsonPath("$.message", is("CSV processing error")));
    }
    
    @Test
    public void uploadUnsupportedFile() throws Exception {
        MockMultipartFile unsupportedFile = new MockMultipartFile("file", "unsupported.unsupported", "text/unsupported", "".getBytes());
        
        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
        		.file(unsupportedFile))
        		.andExpect(status().isBadRequest())
        		.andExpect(jsonPath("$.message", is("Unsupported file")));
    }
    
}