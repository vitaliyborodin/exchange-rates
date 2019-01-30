package com.vborodin.exchangerates.task;

import com.vborodin.exchangerates.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class ExchangeRateTask {

    @Autowired
    private Environment env;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateTask.class);

    @Scheduled(cron = "${exchangerates.scheduler.removeexpiredrates.cron}")
    public void removeExpired() {
        log.info("Task removeExpired started");
        String daysProperty = env.getProperty("exchangerates.scheduler.removeexpiredrates.days");
        if (daysProperty != null && !daysProperty.isEmpty()){
            int days = Integer.parseInt(daysProperty);
            Calendar currentDate = Calendar.getInstance();
            currentDate.add(Calendar.DAY_OF_MONTH, -days);
            log.info("Removed exchange rates count: {}", exchangeRateRepository.deleteByUpdatedDateLessThan(currentDate.getTime()));
        } else {
            log.error("Property not found.");
        }
        log.info("Task removeExpired complete");
    }

}