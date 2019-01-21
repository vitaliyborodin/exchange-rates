package com.vborodin.exchangerates.util;

import com.vborodin.exchangerates.exception.ApiException;
import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.model.ExchangeRateId;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CSVReader implements Reader {
    private static final Logger logger = LoggerFactory.getLogger(CSVReader.class);

    private MultipartFile file;

    CSVReader(MultipartFile file) {
        this.file = file;
    }

    @Override
    public List<ExchangeRate> read() {
        List<ExchangeRate> exchangeRateList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] rateCsv = line.split(",");

                ExchangeRate rateObj = new ExchangeRate(
                        new ExchangeRateId(rateCsv[0], FilenameUtils.getBaseName(file.getOriginalFilename())),
                        new BigDecimal(rateCsv[1]),
                        new BigDecimal(rateCsv[2])
                );

                exchangeRateList.add(rateObj);
            }

        } catch (IOException | IndexOutOfBoundsException e) {
            logger.error(e.toString());
            throw new ApiException("CSV processing error", HttpStatus.BAD_REQUEST);
        }

        return exchangeRateList;
    }
}