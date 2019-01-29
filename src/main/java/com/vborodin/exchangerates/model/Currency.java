package com.vborodin.exchangerates.model;

import com.vborodin.exchangerates.exception.ApiException;
import org.springframework.http.HttpStatus;


public enum Currency {
    UAH, USD, EUR, GBP;

    public static Currency find(String name){
        try {
            return Currency.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ApiException("Currency not found", HttpStatus.NOT_FOUND);
        }
    }

}
