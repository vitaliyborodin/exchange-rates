package com.vborodin.exchangerates.repository;

import com.vborodin.exchangerates.model.Bank;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BankRepository extends PagingAndSortingRepository<Bank, Long> {
    Bank findByName(String name);
}
