package com.vborodin.exchangerates.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vborodin.exchangerates.exception.ApiException;
import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.repository.BankRepository;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class JSONReader implements Reader {
    private static final Logger logger = LoggerFactory.getLogger(JSONReader.class);

    private static final String FILE_TYPE = "JSON";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private BankRepository bankRepository;

    @Override
    public String getFileType() {
        return FILE_TYPE;
    }

    @Override
    public List<ExchangeRate> read(MultipartFile file) {
        List<ExchangeRate> exchangeRateList;
        try {
            exchangeRateList = mapper.readValue(file.getInputStream(), new TypeReference<List<ExchangeRate>>() {
            });
            exchangeRateList.forEach(exchangeRate -> exchangeRate.getId().setBank(bankRepository.findByName(FilenameUtils.getBaseName(file.getOriginalFilename()))));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ApiException("JSON processing error", HttpStatus.BAD_REQUEST);
        }
        return exchangeRateList;
    }
}
