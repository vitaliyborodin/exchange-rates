package com.vborodin.exchangerates.util;

import com.vborodin.exchangerates.model.ExchangeRate;
import com.vborodin.exchangerates.model.ExchangeRateId;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JSONReader implements Reader {
    private MultipartFile file;

    public JSONReader(MultipartFile file) {
        this.file = file;
    }

    @Override
    public List<ExchangeRate> read() {
        BufferedReader br;
        List<ExchangeRate> exchangeRateList = new ArrayList<>();

        try {
            InputStream is = file.getInputStream();

            String input = new BufferedReader(new InputStreamReader(is)).lines()
                    .parallel().collect(Collectors.joining("\n"));

            JSONArray jsonarray = new JSONArray(input);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String currency = jsonobject.getString("currency");
                String buy = jsonobject.getString("buy");
                String sell = jsonobject.getString("sell");

                ExchangeRate rateObj = new ExchangeRate(
                        new ExchangeRateId(currency, FilenameUtils.getBaseName(file.getOriginalFilename())),
                        new BigDecimal(buy),
                        new BigDecimal(sell)
                );

                exchangeRateList.add(rateObj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return exchangeRateList;
    }
}
