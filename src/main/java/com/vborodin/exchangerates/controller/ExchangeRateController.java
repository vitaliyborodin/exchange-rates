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

    ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateController(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @RequestMapping(value = "/exchangerates", method = RequestMethod.GET)
    public Iterable<ExchangeRate> exchangeRates(@RequestParam(value = "currency", required = false) String currency,
                                                @RequestParam(value = "bank", required = false) String bank,
                                                @RequestParam(value = "orderBy", required = false) String orderBy) {

        if(currency != null && bank == null)
            return exchangeRateRepository.findByIdCurrencyIgnoreCase(currency);
        else if(currency == null && bank != null)
            return exchangeRateRepository.findByIdBankIgnoreCase(bank);
        else if(currency != null && bank != null)
            return exchangeRateRepository.findByIdBankIgnoreCaseAndIdCurrencyIgnoreCase(bank, currency);
        else
            return exchangeRateRepository.findAll();
    }

    @RequestMapping(value = "/exchangerates/{currency}/buy", method = RequestMethod.GET)
    public Iterable<ExchangeRate> buy(@PathVariable("currency") String currency,
                                      @RequestParam(value = "orderBy", required = false) String orderBy){
        Sort sort = createSortObject(orderBy);
        return exchangeRateRepository.findByIdCurrencyAndBuyNotNull(currency, sort);
    }

    @RequestMapping(value = "/exchangerates/{currency}/sell", method = RequestMethod.GET)
    public Iterable<ExchangeRate> sell(@PathVariable("currency") String currency,
                                       @RequestParam(value = "orderBy", required = false) String orderBy){
        Sort sort = createSortObject(orderBy);
        return exchangeRateRepository.findByIdCurrencyAndSellNotNull(currency, sort);
    }

    @RequestMapping(value = "/exchangerates/{currency}/buy/{bank}", method = RequestMethod.DELETE)
    public int deleteBuyBycurrencyAndBank(@PathVariable("currency") String currency,
                                            @PathVariable("bank") String bank){
        return exchangeRateRepository.resetBuyByCurrencyAndBank(currency, bank);
    }

    @RequestMapping(value = "/exchangerates/{currency}/sell/{bank}", method = RequestMethod.DELETE)
    public int deleteSellBycurrencyAndBank(@PathVariable("currency") String currency,
                                             @PathVariable("bank") String bank){
        return exchangeRateRepository.resetSellByCurrencyAndBank(currency, bank);
    }

    @RequestMapping(value = "/exchangerates/{currency}/buy/{bank}", method = RequestMethod.PUT)
    public ExchangeRate putBuyByCurrencyAndBank(@PathVariable("currency") String currency,
                         @PathVariable("bank") String bank,
                         @RequestBody String value){
        ExchangeRate rate = exchangeRateRepository.findByIdCurrencyAndIdBank(currency, bank);
        rate.setBuy(new BigDecimal(value));
        return exchangeRateRepository.save(rate);
    }

    @RequestMapping(value = "/exchangerates/{currency}/sell/{bank}", method = RequestMethod.PUT)
    public ExchangeRate putSellByCurrencyAndBank(@PathVariable("currency") String currency,
                                           @PathVariable("bank") String bank,
                                           @RequestBody String value){
        ExchangeRate rate = exchangeRateRepository.findByIdCurrencyAndIdBank(currency, bank);
        rate.setSell(new BigDecimal(value));
        return exchangeRateRepository.save(rate);
    }

    @RequestMapping(value = "/exchangerates/buy/{bank}", method = RequestMethod.DELETE)
    public int deleteBuyByBank(@PathVariable("bank") String bank){
        return exchangeRateRepository.resetBuyByBank(bank);
    }

    @RequestMapping(value = "/exchangerates/sell/{bank}", method = RequestMethod.DELETE)
    public int deleteSellByBank(@PathVariable("bank") String bank){
        return exchangeRateRepository.resetSellByBank(bank);
    }

    @RequestMapping(value = "/exchangerates/{currency}/bestbuy", method = RequestMethod.GET)
    public ExchangeRate bestBuy(@PathVariable("currency") String currency){
        return exchangeRateRepository.findTopByIdCurrencyOrderByBuyDesc(currency);
    }

    @RequestMapping(value = "/exchangerates/{currency}/bestsell", method = RequestMethod.GET)
    public ExchangeRate bestSell(@PathVariable("currency") String currency){
        return exchangeRateRepository.findTopByIdCurrencyAndSellNotNullOrderBySellAsc(currency);
    }

    @RequestMapping(value = "/exchangerates/report", method = RequestMethod.GET)
    public List<BestRate> report(){
        List<BestRate> result = new ArrayList<>();
        Iterable<ExchangeRate> allRates = exchangeRateRepository.findAll();

        List<String> currencies = new ArrayList<>();
        for (ExchangeRate er : allRates){
            currencies.add(er.getId().getCurrency());
        }

        currencies = new ArrayList<String>(new HashSet<String>(currencies));

        for (String c : currencies) {
            System.out.println(c);
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

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
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

        return new Sort(new Sort.Order(Sort.Direction.valueOf(orderType), orderField));
    }

}
