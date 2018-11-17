package com.vborodin.exchangerates.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ReaderFactory {
    public static Reader getReader(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        System.out.println(extension);

        Reader reader = null;
        if (extension.equalsIgnoreCase("XML")) {
            reader = new XMLReader(file);
        } else if (extension.equalsIgnoreCase("CSV")) {
            reader = new CSVReader(file);
        } else if (extension.equalsIgnoreCase("JSON")) {
            reader = new JSONReader(file);
        }
        return reader;
    }
}