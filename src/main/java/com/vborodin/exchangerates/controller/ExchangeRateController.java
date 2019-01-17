package com.vborodin.exchangerates.controller;

import com.vborodin.exchangerates.model.BestRate;
import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.repository.ExchangeRateRepository;
import com.vborodin.exchangerates.util.ReaderFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@RequestMapping("api/v1")
@RestController
public class ExchangeRateController {

    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateController(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @GetMapping(value = "/exchangerates")
    public Iterable<ExchangeRate> exchangeRates(@RequestParam(value = "currency", required = false) String currency,
                                                @RequestParam(value = "bank", required = false) String bank) {

        if(currency != null && bank == null)
            return exchangeRateRepository.findByIdCurrencyIgnoreCase(currency);
        else if(currency == null && bank != null)
            return exchangeRateRepository.findByIdBankIgnoreCase(bank);
        else if(currency != null && bank != null)
            return exchangeRateRepository.findByIdBankIgnoreCaseAndIdCurrencyIgnoreCase(bank, currency);
        else
            return exchangeRateRepository.findAll();
    }

    @GetMapping(value = "/exchangerates/{currency}/buy")
    public Iterable<ExchangeRate> buy(@PathVariable("currency") String currency,
                                      @RequestParam(value = "orderBy", required = false) String orderBy){
        Sort sort = createSortObject(orderBy);
        return exchangeRateRepository.findByIdCurrencyAndBuyNotNull(currency, sort);
    }

    @GetMapping(value = "/exchangerates/{currency}/sell")
    public Iterable<ExchangeRate> sell(@PathVariable("currency") String currency,
                                       @RequestParam(value = "orderBy", required = false) String orderBy){
        Sort sort = createSortObject(orderBy);
        return exchangeRateRepository.findByIdCurrencyAndSellNotNull(currency, sort);
    }

    @DeleteMapping(value = "/exchangerates/{currency}/buy/{bank}")
    public int deleteBuyBycurrencyAndBank(@PathVariable("currency") String currency,
                                            @PathVariable("bank") String bank){
        return exchangeRateRepository.resetBuyByCurrencyAndBank(currency, bank);
    }

    @DeleteMapping(value = "/exchangerates/{currency}/sell/{bank}")
    public int deleteSellBycurrencyAndBank(@PathVariable("currency") String currency,
                                             @PathVariable("bank") String bank){
        return exchangeRateRepository.resetSellByCurrencyAndBank(currency, bank);
    }

    @PutMapping(value = "/exchangerates/{currency}/buy/{bank}")
    public ExchangeRate putBuyByCurrencyAndBank(@PathVariable("currency") String currency,
                         @PathVariable("bank") String bank,
                         @RequestBody String value){
        ExchangeRate rate = exchangeRateRepository.findByIdCurrencyAndIdBank(currency, bank);
        rate.setBuy(new BigDecimal(value));
        return exchangeRateRepository.save(rate);
    }

    @PutMapping(value = "/exchangerates/{currency}/sell/{bank}")
    public ExchangeRate putSellByCurrencyAndBank(@PathVariable("currency") String currency,
                                           @PathVariable("bank") String bank,
                                           @RequestBody String value){
        ExchangeRate rate = exchangeRateRepository.findByIdCurrencyAndIdBank(currency, bank);
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
        return exchangeRateRepository.findTopByIdCurrencyOrderByBuyDesc(currency);
    }

    @GetMapping(value = "/exchangerates/{currency}/bestsell")
    public ExchangeRate bestSell(@PathVariable("currency") String currency){
        return exchangeRateRepository.findTopByIdCurrencyAndSellNotNullOrderBySellAsc(currency);
    }

    @GetMapping(value = "/exchangerates/report")
    public List<BestRate> report(){
        List<BestRate> result = new ArrayList<>();
        Iterable<ExchangeRate> allRates = exchangeRateRepository.findAll();

        List<String> currencies = new ArrayList<>();
        for (ExchangeRate er : allRates){
            currencies.add(er.getId().getCurrency());
        }

        currencies = new ArrayList<>(new HashSet<>(currencies));

        for (String c : currencies) {
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

    @PostMapping(value = "/upload")
    public Iterable<ExchangeRate> handleFileUpload(@RequestParam("file") MultipartFile file) {

        List<ExchangeRate> exchangeRates = ReaderFactory.getReader(file).read();
        for (ExchangeRate er : exchangeRates){
            exchangeRateRepository.save(er);
        }

        return exchangeRateRepository.findByIdBankIgnoreCase(FilenameUtils.getBaseName(file.getOriginalFilename()));
    }

    private Sort createSortObject(String orderBy){
        String orderField = "bank";
        String orderType = "DESC";

        String[] parts = orderBy.split(",");
        if (parts.length > 0) orderField = parts[0];
        if (parts.length > 1) orderType = parts[1].toUpperCase();

        return Sort.by(new Sort.Order(Sort.Direction.valueOf(orderType), orderField));
    }

}
