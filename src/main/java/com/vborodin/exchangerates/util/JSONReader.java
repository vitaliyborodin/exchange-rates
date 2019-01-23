package com.vborodin.exchangerates.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vborodin.exchangerates.exception.ApiException;
import com.vborodin.exchangerates.model.ExchangeRate;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class JSONReader implements Reader {
    private static final Logger logger = LoggerFactory.getLogger(JSONReader.class);

    private MultipartFile file;

    JSONReader(MultipartFile file) {
        this.file = file;
    }

    @Override
    public List<ExchangeRate> read() {
        List<ExchangeRate> exchangeRateList;
        try {
            exchangeRateList = new ObjectMapper().readValue(file.getInputStream(), new TypeReference<List<ExchangeRate>>(){});
            exchangeRateList.forEach(exchangeRate -> exchangeRate.getId().setBank(FilenameUtils.getBaseName(file.getOriginalFilename())));
        } catch (IOException e) {
        	logger.error(e.getMessage());
            throw new ApiException("JSON processing error", HttpStatus.BAD_REQUEST);
        }
        return exchangeRateList;
    }
}
