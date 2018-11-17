package com.vborodin.exchangerates.util;

import com.vborodin.exchangerates.model.ExchangeRate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Reader {
    public List<ExchangeRate> read();
}