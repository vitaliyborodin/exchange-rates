package com.vborodin.exchangerates.util;

import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.model.ExchangeRateId;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CSVReader implements Reader {

    private MultipartFile file;

    public CSVReader(MultipartFile file) {
        this.file = file;
    }

    @Override
    public List<ExchangeRate> read() {
        BufferedReader br;
        List<ExchangeRate> exchangeRateList = new ArrayList<>();

        try {
            String line;
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                String[] rateCsv = line.split(",");

                ExchangeRate rateObj = new ExchangeRate(
                        new ExchangeRateId(rateCsv[0], FilenameUtils.getBaseName(file.getOriginalFilename())),
                        new BigDecimal(rateCsv[1]),
                        new BigDecimal(rateCsv[2])
                );

                exchangeRateList.add(rateObj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return exchangeRateList;
    }
}