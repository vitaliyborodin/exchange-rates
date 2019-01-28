package com.vborodin.exchangerates.model;

import java.util.Arrays;

public enum Currency {
    UAH, USD, EUR, GBP;

    public static Currency find(String name){
        return Arrays.stream(Currency.values())
            .filter(value -> value.toString().equalsIgnoreCase(name))
            .findFirst().orElse(null);
    }

}
