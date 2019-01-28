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
import java.util.HashSet;
import java.util.List;


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
                                                @RequestParam(value = "bank", required = false) String bank) {

        Currency currency = Currency.find(currencyName);

        if(currencyName != null && bank == null)
            return exchangeRateRepository.findByIdCurrency(currency);
        else if(currencyName == null && bank != null)
            return exchangeRateRepository.findByIdBankNameIgnoreCase(bank);
        else if(currencyName != null)
            return exchangeRateRepository.findByIdBankNameIgnoreCaseAndIdCurrency(bank, currency);
        else
            return exchangeRateRepository.findAll();
    }

    @GetMapping(value = "/exchangerates/{currency}/buy")
    public Iterable<ExchangeRate> buy(@PathVariable("currency") String currency,
                                      @RequestParam(value = "orderBy", required = false) String orderBy){
        Sort sort = createSortObject(orderBy);
        return exchangeRateRepository.findByIdCurrencyAndBuyNotNull(Currency.valueOf(currency.toUpperCase()), sort);
    }

    @GetMapping(value = "/exchangerates/{currency}/sell")
    public Iterable<ExchangeRate> sell(@PathVariable("currency") String currency,
                                       @RequestParam(value = "orderBy", required = false) String orderBy){
        Sort sort = createSortObject(orderBy);
        return exchangeRateRepository.findByIdCurrencyAndSellNotNull(Currency.valueOf(currency.toUpperCase()), sort);
    }

    @DeleteMapping(value = "/exchangerates/{currency}/buy/{bank}")
    public int deleteBuyBycurrencyAndBank(@PathVariable("currency") String currency,
                                            @PathVariable("bank") String bank){
        return exchangeRateRepository.resetBuyByCurrencyAndBank(Currency.valueOf(currency.toUpperCase()), bank);
    }

    @DeleteMapping(value = "/exchangerates/{currency}/sell/{bank}")
    public int deleteSellBycurrencyAndBank(@PathVariable("currency") String currency,
                                             @PathVariable("bank") String bank){
        return exchangeRateRepository.resetSellByCurrencyAndBank(Currency.valueOf(currency.toUpperCase()), bank);
    }

    @PutMapping(value = "/exchangerates/{currency}/buy/{bank}")
    public ExchangeRate putBuyByCurrencyAndBank(@PathVariable("currency") String currency,
                         @PathVariable("bank") String bank,
                         @RequestBody String value){
        ExchangeRate rate = exchangeRateRepository.findByIdCurrencyAndIdBankNameIgnoreCase(Currency.valueOf(currency.toUpperCase()), bank);
        rate.setBuy(new BigDecimal(value));
        return exchangeRateRepository.save(rate);
    }

    @PutMapping(value = "/exchangerates/{currency}/sell/{bank}")
    public ExchangeRate putSellByCurrencyAndBank(@PathVariable("currency") String currency,
                                           @PathVariable("bank") String bank,
                                           @RequestBody String value){
        ExchangeRate rate = exchangeRateRepository.findByIdCurrencyAndIdBankNameIgnoreCase(Currency.valueOf(currency.toUpperCase()), bank);
        rate.setSell(new BigDecimal(value));
        return exchangeRateRepository.save(rate);
    }

    @DeleteMapping(value = "/exchangerates/buy/{bank}")
    public int deleteBuyByBank(@PathVariable("bank") String bank){
        return exchangeRateRepository.resetBuyByBank(bank);
    }

    @DeleteMapping(value = "/exchangerates/sell/{bank}")
    public int deleteSellByBank(@PathVariable("bank") String bank){
        return exchangeRateRepository.resetSellByBank(bank);
    }

    @GetMapping(value = "/exchangerates/{currency}/bestbuy")
    public ExchangeRate bestBuy(@PathVariable("currency") String currency){
        return exchangeRateRepository.findTopByIdCurrencyOrderByBuyDesc(Currency.valueOf(currency.toUpperCase()));
    }

    @GetMapping(value = "/exchangerates/{currency}/bestsell")
    public ExchangeRate bestSell(@PathVariable("currency") String currency){
        return exchangeRateRepository.findTopByIdCurrencyAndSellNotNullOrderBySellAsc(Currency.valueOf(currency.toUpperCase()));
    }

    @GetMapping(value = "/exchangerates/report")
    public List<BestRate> report(){
        List<BestRate> result = new ArrayList<>();
        Iterable<ExchangeRate> allRates = exchangeRateRepository.findAll();

        List<Currency> currencies = new ArrayList<>();
        for (ExchangeRate er : allRates){
            currencies.add(er.getId().getCurrency());
        }

        currencies = new ArrayList<>(new HashSet<>(currencies));

        for (Currency c : currencies) {
            BestRate item = new BestRate();
            item.setCurrency(c);
            ExchangeRate bestBuyRate = exchangeRateRepository.findTopByIdCurrencyOrderByBuyDesc(c);
            item.setBuyRate(bestBuyRate.getBuy());
            item.setBuyBank(bestBuyRate.getId().getBank());
            ExchangeRate bestSellRate = exchangeRateRepository.findTopByIdCurrencyAndSellNotNullOrderBySellAsc(c);
            item.setSellRate(bestSellRate.getSell());
            item.setSellBank(bestSellRate.getId().getBank());

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

    private Sort createSortObject(String orderBy){
    	if (orderBy == null) return null;
    	
    	String orderField = "bank";
        String orderType = "DESC";

        String[] parts = orderBy.split(",");
        if (parts.length > 0) orderField = parts[0];
        if (parts.length > 1) orderType = parts[1].toUpperCase();

        return Sort.by(new Sort.Order(Sort.Direction.valueOf(orderType), orderField));
    }

}
