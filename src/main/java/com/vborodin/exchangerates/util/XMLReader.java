package com.vborodin.exchangerates.util;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vborodin.exchangerates.exception.ApiException;
import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.model.Rates;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class XMLReader implements Reader {
    private static final Logger logger = LoggerFactory.getLogger(XMLReader.class);

    private static final String FILE_TYPE = "XML";

    private static final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public String getFileType() {
        return FILE_TYPE;
    }

    @Override
    public List<ExchangeRate> read(MultipartFile file) {
        List<ExchangeRate> exchangeRateList;
        try {
            Rates rates = xmlMapper.readValue(file.getInputStream(), Rates.class);
            exchangeRateList = rates.getRates().stream()
                    .peek(exchangeRate -> exchangeRate.getId().setBank(FilenameUtils.getBaseName(file.getOriginalFilename())))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ApiException("XML processing error", HttpStatus.BAD_REQUEST);
        }
        return exchangeRateList;
    }
}
