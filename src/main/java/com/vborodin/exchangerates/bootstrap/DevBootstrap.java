package com.vborodin.exchangerates.bootstrap;

import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.model.ExchangeRateId;
import com.vborodin.exchangerates.repository.ExchangeRateRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private ExchangeRateRepository exchangeRateRepository;

    public DevBootstrap(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        initData();
    }

    private void initData(){
        ExchangeRate rate = new ExchangeRate(
                new ExchangeRateId("UAH", "DEV BANK"),
                BigDecimal.valueOf(1.11d),
                BigDecimal.ONE
        );

        exchangeRateRepository.save(rate);

        rate = new ExchangeRate(
                new ExchangeRateId("USD", "DEV BANK"),
                BigDecimal.TEN,
                BigDecimal.valueOf(10.1)
        );

        exchangeRateRepository.save(rate);

        rate = new ExchangeRate(
                new ExchangeRateId("UAH", "DEV BANK2"),
                BigDecimal.valueOf(1.10d),
                BigDecimal.valueOf(1.11d)
        );

        exchangeRateRepository.save(rate);
    }
}
