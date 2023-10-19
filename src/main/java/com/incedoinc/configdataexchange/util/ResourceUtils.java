package com.incedoinc.configdataexchange.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ResourceUtils {

    public static Resource getResource(String location, ResourceLoader resourceLoader) {
        Resource resource;
        if (location.startsWith("classpath:")) {
            resource = new ClassPathResource(location.substring("classpath:".length()));
        } else if (location.startsWith("file:")) {
            resource = resourceLoader.getResource(location);
        } else {
            resource = resourceLoader.getResource("file:" + location);
        }
        return resource;
    }
}