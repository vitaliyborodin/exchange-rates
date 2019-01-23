package com.vborodin.exchangerates.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.vborodin.exchangerates.exception.ApiException;
import com.vborodin.exchangerates.model.ExchangeRate;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

        try {
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
            csvMapper.enable(CsvParser.Feature.INSERT_NULLS_FOR_MISSING_COLUMNS);
            CsvSchema csvSchema = csvMapper.schemaFor(ExchangeRate.class).withHeader().sortedBy("currency","buy","sell");

            MappingIterator<ExchangeRate> iterator = csvMapper.readerFor(ExchangeRate.class).with(csvSchema).readValues(file.getInputStream());
            while (iterator.hasNext()) {
                ExchangeRate rate = iterator.nextValue();
                if (rate.getId().getCurrency() == null
                        || (rate.getBuy() == null && rate.getSell() == null)) {
                    throw new ApiException("CSV processing error", HttpStatus.BAD_REQUEST);
                } else {
                    rate.getId().setBank(FilenameUtils.getBaseName(file.getOriginalFilename()));
                    exchangeRateList.add(rate);
                }
            }

            return exchangeRateList;
        } catch (IndexOutOfBoundsException | IllegalArgumentException | IOException e) {
            logger.error(e.toString());
            throw new ApiException("CSV processing error", HttpStatus.BAD_REQUEST);
        }
    }
}