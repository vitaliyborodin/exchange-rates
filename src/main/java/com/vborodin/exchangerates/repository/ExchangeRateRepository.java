package com.vborodin.exchangerates.repository;

import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.model.ExchangeRateId;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ExchangeRateRepository extends PagingAndSortingRepository<ExchangeRate, ExchangeRateId> {

    Iterable<ExchangeRate> findByIdBankIgnoreCase(String bank);
    Iterable<ExchangeRate> findByIdCurrencyIgnoreCase(String currency);
    Iterable<ExchangeRate> findByIdBankIgnoreCaseAndIdCurrencyIgnoreCase(String bank, String currency);
    ExchangeRate findTopByIdCurrencyOrderByBuyDesc(String currenct);
    ExchangeRate findTopByIdCurrencyAndSellNotNullOrderBySellAsc(String currenct);
    Iterable<ExchangeRate> findByIdCurrencyAndBuyNotNull(String currency, Sort sort);
    Iterable<ExchangeRate> findByIdCurrencyAndSellNotNull(String currency, Sort sort);

    @Transactional
    @Modifying
    @Query("update ExchangeRate o set o.sell = null where o.id.currency =?1 and o.id.bank = ?2")
    int resetSellByCurrencyAndBank(String currency, String bank);

    @Transactional
    @Modifying
    @Query("update ExchangeRate o set o.sell = null where o.id.currency =?1 and o.id.bank = ?2")
    int resetBuyByCurrencyAndBank(String currency, String bank);

    @Transactional
    @Modifying
    @Query("update ExchangeRate o set o.sell = null where o.id.bank = ?1")
    int resetSellByBank(String bank);

    @Transactional
    @Modifying
    @Query("update ExchangeRate o set o.buy = null where o.id.bank = ?1")
    int resetBuyByBank(String bank);

}
