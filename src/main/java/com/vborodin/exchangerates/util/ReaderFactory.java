package com.vborodin.exchangerates.util;

import com.vborodin.exchangerates.exception.ApiException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReaderFactory {
    private static final Logger logger = LoggerFactory.getLogger(ReaderFactory.class);
    private Map<String, Reader> readers;

    @Autowired(required = false)
    private ReaderFactory(Map<String, Reader> readers) {
        this.readers = readers.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getValue().getFileType().toUpperCase(),
                        Map.Entry::getValue
                ));
    }

    @PostConstruct
    public void init() {
        logger.info("Injected readers: {}", readers.keySet());
    }

    public Reader getReader(MultipartFile file) {
         return Optional.ofNullable(readers.get(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase()))
                 .orElseThrow(() -> new ApiException("Unsupported file", HttpStatus.BAD_REQUEST));
    }
}