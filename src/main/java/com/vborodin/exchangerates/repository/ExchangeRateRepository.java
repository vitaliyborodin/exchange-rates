package com.vborodin.exchangerates.repository;

import com.vborodin.exchangerates.model.Currency;
import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.model.ExchangeRateId;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ExchangeRateRepository extends PagingAndSortingRepository<ExchangeRate, ExchangeRateId> {

    Iterable<ExchangeRate> findAll();
    Iterable<ExchangeRate> findByIdBankNameIgnoreCase(String bank);
    Iterable<ExchangeRate> findByIdCurrency(Currency currency);
    Iterable<ExchangeRate> findByIdBankNameIgnoreCaseAndIdCurrency(String bank, Currency currency);
    ExchangeRate findTopByIdCurrencyOrderByBuyDesc(Currency currency);
    ExchangeRate findTopByIdCurrencyAndSellNotNullOrderBySellAsc(Currency currency);
    Iterable<ExchangeRate> findByIdCurrencyAndBuyNotNull(Currency currency, Sort sort);
    Iterable<ExchangeRate> findByIdCurrencyAndSellNotNull(Currency currency, Sort sort);
    ExchangeRate findByIdCurrencyAndIdBankNameIgnoreCase(Currency currency, String bank);

    @Transactional
    @Modifying
    @Query("update ExchangeRate o set o.sell = null where o.id.currency =?1 and o.id.bank = ?2")
    int resetSellByCurrencyAndBank(Currency currency, String bank);

    @Transactional
    @Modifying
    @Query("update ExchangeRate o set o.sell = null where o.id.currency =?1 and o.id.bank = ?2")
    int resetBuyByCurrencyAndBank(Currency currency, String bank);

    @Transactional
    @Modifying
    @Query("update ExchangeRate o set o.sell = null where o.id.bank = ?1")
    int resetSellByBank(String bank);

    @Transactional
    @Modifying
    @Query("update ExchangeRate o set o.buy = null where o.id.bank = ?1")
    int resetBuyByBank(String bank);

}
