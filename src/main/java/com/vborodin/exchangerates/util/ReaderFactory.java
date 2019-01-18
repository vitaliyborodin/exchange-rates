package com.vborodin.exchangerates.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.vborodin.exchangerates.exception.ApiException;

public class ReaderFactory {
    private ReaderFactory() {
    }

    public static Reader getReader(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        Reader reader;
        if (extension.equalsIgnoreCase("XML")) {
            reader = new XMLReader(file);
        } else if (extension.equalsIgnoreCase("CSV")) {
            reader = new CSVReader(file);
        } else if (extension.equalsIgnoreCase("JSON")) {
            reader = new JSONReader(file);
        } else throw new ApiException("Unsupported file");
        return reader;
    }
}