package com.vborodin.exchangerates.controller;

import com.vborodin.exchangerates.model.BestRate;
import com.vborodin.exchangerates.model.Currency;
import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.repository.ExchangeRateRepository;
import com.vborodin.exchangerates.util.ReaderFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequestMapping(value = "api/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class ExchangeRateController {

    private ExchangeRateRepository exchangeRateRepository;
    private ReaderFactory readerFactory;

    @Autowired
    public ExchangeRateController(ExchangeRateRepository exchangeRateRepository, ReaderFactory readerFactory) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.readerFactory = readerFactory;
    }

    @GetMapping(value = "/exchangerates")
    public Iterable<ExchangeRate> exchangeRates(@RequestParam(value = "currency", required = false) String currencyName,
                                                @RequestParam(value = "bank", required = false) String bankName) {

        return Optional.ofNullable(currencyName)
                .map(currency ->  Optional.ofNullable(bankName)
                        .map(bank -> exchangeRateRepository.findByIdBankNameIgnoreCaseAndIdCurrency(bank, Currency.find(currencyName)))
                        .orElseGet(() -> exchangeRateRepository.findByIdCurrency(Currency.find(currencyName))))
                .orElseGet(() ->  Optional.ofNullable(bankName)
                        .map(bank -> exchangeRateRepository.findByIdBankNameIgnoreCase(bank))
                        .orElseGet(() -> exchangeRateRepository.findAll()));
    }

    @GetMapping(value = "/exchangerates/{currency}/buy")
    public Iterable<ExchangeRate> buy(@PathVariable("currency") String currency,
                                      @RequestParam(value = "orderBy", required = false) String orderBy) {
        Sort sort = createSortObject(orderBy);
        return exchangeRateRepository.findByIdCurrencyAndBuyNotNull(Currency.find(currency.toUpperCase()), sort);
    }

    @GetMapping(value = "/exchangerates/{currency}/sell")
    public Iterable<ExchangeRate> sell(@PathVariable("currency") String currency,
                                       @RequestParam(value = "orderBy", required = false) String orderBy) {
        Sort sort = createSortObject(orderBy);
        return exchangeRateRepository.findByIdCurrencyAndSellNotNull(Currency.find(currency.toUpperCase()), sort);
    }

    @DeleteMapping(value = "/exchangerates/{currency}/buy/{bank}")
    public int deleteBuyBycurrencyAndBank(@PathVariable("currency") String currency,
                                          @PathVariable("bank") String bank) {
        return exchangeRateRepository.resetBuyByCurrencyAndBank(Currency.find(currency.toUpperCase()), bank);
    }

    @DeleteMapping(value = "/exchangerates/{currency}/sell/{bank}")
    public int deleteSellBycurrencyAndBank(@PathVariable("currency") String currency,
                                           @PathVariable("bank") String bank) {
        return exchangeRateRepository.resetSellByCurrencyAndBank(Currency.find(currency.toUpperCase()), bank);
    }

    @PutMapping(value = "/exchangerates/{currency}/buy/{bank}")
    public ExchangeRate putBuyByCurrencyAndBank(@PathVariable("currency") String currency,
                                                @PathVariable("bank") String bank,
                                                @RequestBody String value) {
        ExchangeRate rate = exchangeRateRepository.findByIdCurrencyAndIdBankNameIgnoreCase(Currency.find(currency.toUpperCase()), bank);
        rate.setBuy(new BigDecimal(value));
        return exchangeRateRepository.save(rate);
    }

    @PutMapping(value = "/exchangerates/{currency}/sell/{bank}")
    public ExchangeRate putSellByCurrencyAndBank(@PathVariable("currency") String currency,
                                                 @PathVariable("bank") String bank,
                                                 @RequestBody String value) {
        ExchangeRate rate = exchangeRateRepository.findByIdCurrencyAndIdBankNameIgnoreCase(Currency.find(currency.toUpperCase()), bank);
        rate.setSell(new BigDecimal(value));
        return exchangeRateRepository.save(rate);
    }

    @DeleteMapping(value = "/exchangerates/buy/{bank}")
    public int deleteBuyByBank(@PathVariable("bank") String bank) {
        return exchangeRateRepository.resetBuyByBank(bank);
    }

    @DeleteMapping(value = "/exchangerates/sell/{bank}")
    public int deleteSellByBank(@PathVariable("bank") String bank) {
        return exchangeRateRepository.resetSellByBank(bank);
    }

    @GetMapping(value = "/exchangerates/{currency}/bestbuy")
    public ExchangeRate bestBuy(@PathVariable("currency") String currency) {
        return exchangeRateRepository.findTopByIdCurrencyOrderByBuyDesc(Currency.find(currency.toUpperCase()));
    }

    @GetMapping(value = "/exchangerates/{currency}/bestsell")
    public ExchangeRate bestSell(@PathVariable("currency") String currency) {
        return exchangeRateRepository.findTopByIdCurrencyAndSellNotNullOrderBySellAsc(Currency.find(currency.toUpperCase()));
    }

    @GetMapping(value = "/exchangerates/report")
    public List<BestRate> report() {
        List<BestRate> result = new ArrayList<>();

        for (Currency c : Currency.values()) {
            BestRate item = new BestRate(c);
            item.setBuy(exchangeRateRepository.findTopByIdCurrencyOrderByBuyDesc(c));
            item.setSell(exchangeRateRepository.findTopByIdCurrencyAndSellNotNullOrderBySellAsc(c));
            result.add(item);
        }
        return result;
    }

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Iterable<ExchangeRate> handleFileUpload(@RequestParam("file") MultipartFile file) {
        readerFactory.getReader(file).read(file)
                .forEach(exchangeRateRepository::save);

        return exchangeRateRepository.findByIdBankNameIgnoreCase(FilenameUtils.getBaseName(file.getOriginalFilename()));
    }

    private Sort createSortObject(String orderBy) {
        if (orderBy == null) return null;

        String orderField = "bank";
        String orderType = "DESC";

        String[] parts = orderBy.split(",");
        if (parts.length > 0) orderField = parts[0];
        if (parts.length > 1) orderType = parts[1].toUpperCase();

        return Sort.by(new Sort.Order(Sort.Direction.valueOf(orderType), orderField));
    }

}
