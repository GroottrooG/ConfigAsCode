package com.incedoinc.configdataexchange.fetcher;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileFetcherFromMultipartFile implements PropertiesFetcher {

    private final MultipartFile file;

    public PropertiesFileFetcherFromMultipartFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public Properties fetchProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = file.getInputStream()) {
            properties.load(inputStream);
        }
        return properties;
    }

    @Override
    public String getResourceName() {
        return file.getOriginalFilename();
    }
}
