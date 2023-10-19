package com.incedoinc.configdataexchange;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.incedoinc.configdataexchange.converter.PropertiesToJsonConverter;
import com.incedoinc.configdataexchange.converter.PropertiesToJsonConverterImpl;
import com.incedoinc.configdataexchange.fetcher.PropertiesFetcher;
import com.incedoinc.configdataexchange.fetcher.PropertiesFileFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ResourceLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
@Slf4j
public class ConfigDataExchangeApplication implements CommandLineRunner {

    private final ResourceLoader resourceLoader;

    public ConfigDataExchangeApplication(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigDataExchangeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("ConfigDataExchangeApplication started successfully");
        String classpathResource = "classpath:application.properties";
        String filePath = "C:\\Users\\nizam\\Downloads\\configdataexchange\\configdataexchange\\src\\main\\resources\\configs\\a.yml";
        // Fetch properties from different sources
        Map<String, Properties> propertiesMap = new HashMap<>();
        propertiesMap.put(classpathResource, fetchProperties(classpathResource));
        propertiesMap.put(filePath, fetchProperties(filePath));
        // Inside your run method or wherever you are processing the properties
        PropertiesToJsonConverter converter = new PropertiesToJsonConverterImpl();
        String json2 = converter.convertAllToJson(propertiesMap);
        System.out.println(json2);

    }


    private Properties fetchProperties(String resourceLocation) throws IOException {
        PropertiesFetcher propertiesFetcher = new PropertiesFileFetcher(resourceLocation, resourceLoader);
        return propertiesFetcher.fetchProperties();
    }
}
