package com.vborodin.exchangerates.task;

import com.vborodin.exchangerates.ExchangeRatesApplication;
import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.repository.BankRepository;
import com.vborodin.exchangerates.repository.ExchangeRateRepository;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.randomizers.range.LongRangeRandomizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ExchangeRatesApplication.class})
@TestPropertySource(properties = {
        "exchangerates.scheduler.removeexpiredrates.cron=*/5 * * * * *",
        "exchangerates.scheduler.removeexpiredrates.days=0",
})
public class ExchangeRateTaskTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();

        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                .randomize(Long.class, new LongRangeRandomizer(0L, 100L))
                .build();

        ExchangeRate exchangeRate = enhancedRandom.nextObject(ExchangeRate.class);
        exchangeRate.getId().setBank(bankRepository.save(exchangeRate.getId().getBank()));
        exchangeRateRepository.save(exchangeRate);
    }

    @Test
    public void removeExpired() {
        long initialCount = exchangeRateRepository.count();
        await().atMost(3, TimeUnit.SECONDS)
                .untilAsserted( () -> assertEquals(1, initialCount - exchangeRateRepository.count()) );
    }

}