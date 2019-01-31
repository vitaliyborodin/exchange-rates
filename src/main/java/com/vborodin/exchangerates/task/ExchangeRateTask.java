package com.vborodin.exchangerates.task;

import com.vborodin.exchangerates.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExchangeRateTask {

    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateTask(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Value("${exchangerates.scheduler.removeexpiredrates.days:1}")
    private long days;

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateTask.class);

    @Scheduled(cron = "${exchangerates.scheduler.removeexpiredrates.cron}")
    public void removeExpired() {
        log.info("Task removeExpired started");
        log.info("Removed exchange rates count: {}", exchangeRateRepository.deleteByUpdatedDateLessThan(LocalDateTime.now().minusDays(days)));

    }

}