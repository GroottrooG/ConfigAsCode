package com.incedoinc.configdataexchange.fetcher;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PropertiesFetcherFactory {

    private final ResourceLoader resourceLoader;

    public PropertiesFetcherFactory(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public PropertiesFetcher getPropertiesFetcher(String filename) {
        if (filename.endsWith(".yaml") || filename.endsWith(".yml")) {
            return new YamlFileFetcher(filename, resourceLoader);
        } else {
            return new PropertiesFileFetcher(filename, resourceLoader);
        }
    }

    public PropertiesFetcher getPropertiesFetcher(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null) {
            if (filename.endsWith(".yaml") || filename.endsWith(".yml")) {
                return new YamlFileFetcherFromMultipartFile(file);
            } else {
                return new PropertiesFileFetcherFromMultipartFile(file);
            }
        } else {
            throw new IllegalArgumentException("File has no name");
        }
    }
}
