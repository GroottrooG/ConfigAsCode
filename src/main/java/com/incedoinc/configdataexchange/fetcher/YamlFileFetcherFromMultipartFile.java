package com.incedoinc.configdataexchange.fetcher;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Properties;

public class YamlFileFetcherFromMultipartFile implements PropertiesFetcher {
    private final MultipartFile file;

    public YamlFileFetcherFromMultipartFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public Properties fetchProperties() throws IOException {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new ByteArrayResource(file.getBytes()));
        return yamlFactory.getObject();
    }

    @Override
    public String getResourceName() {
        return file.getOriginalFilename();
    }
}
