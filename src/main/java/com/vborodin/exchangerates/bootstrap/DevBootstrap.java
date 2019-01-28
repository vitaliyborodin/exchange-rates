package com.vborodin.exchangerates.bootstrap;

import com.vborodin.exchangerates.model.Bank;
import com.vborodin.exchangerates.model.Currency;
import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.model.ExchangeRateId;
import com.vborodin.exchangerates.repository.BankRepository;
import com.vborodin.exchangerates.repository.ExchangeRateRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("dev")
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private ExchangeRateRepository exchangeRateRepository;
    private BankRepository bankRepository;

    public DevBootstrap(ExchangeRateRepository exchangeRateRepository, BankRepository bankRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        initData();
    }

    private void initData(){
        Bank bank = new Bank("DEV BANK");
        Bank bank2 = new Bank("DEV BANK2");
        Bank bank3 = new Bank("JSON BANK");
        Bank bank4 = new Bank("XML BANK");
        Bank bank5 = new Bank("CSV BANK");

        bankRepository.save(bank);
        bankRepository.save(bank2);
        bankRepository.save(bank3);
        bankRepository.save(bank4);
        bankRepository.save(bank5);

        ExchangeRate rate = new ExchangeRate(
                new ExchangeRateId(Currency.UAH, bank),
                BigDecimal.valueOf(1.11d),
                BigDecimal.ONE
        );

        exchangeRateRepository.save(rate);

        rate = new ExchangeRate(
                new ExchangeRateId(Currency.USD, bank),
                BigDecimal.TEN,
                BigDecimal.valueOf(10.1)
        );

        exchangeRateRepository.save(rate);

        rate = new ExchangeRate(
                new ExchangeRateId(Currency.UAH, bank2),
                BigDecimal.valueOf(1.10d),
                BigDecimal.valueOf(1.11d)
        );

        exchangeRateRepository.save(rate);
    }
}
