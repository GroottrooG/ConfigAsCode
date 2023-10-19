package com.incedoinc.configdataexchange.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.incedoinc.configdataexchange.converter.PropertiesToJsonConverter;
import com.incedoinc.configdataexchange.converter.PropertiesToJsonConverterImpl;
import com.incedoinc.configdataexchange.fetcher.PropertiesFetcher;
import com.incedoinc.configdataexchange.fetcher.PropertiesFetcherFactory;
import com.incedoinc.configdataexchange.service.PropertiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * REST controller for converting properties files to JSON.
 */
@RestController
@RequestMapping("/api/properties")
@Tag(name = "Properties to JSON Converter", description = "Converts properties files to JSON")
@Slf4j
public class PropertiesController {

    private final ResourceLoader resourceLoader;
    private final PropertiesFetcherFactory propertiesFetcherFactory;

    private final PropertiesService propertiesService;

    public PropertiesController(ResourceLoader resourceLoader, PropertiesFetcherFactory propertiesFetcherFactory, PropertiesService propertiesService) {
        this.resourceLoader = resourceLoader;
        this.propertiesFetcherFactory = propertiesFetcherFactory;
        this.propertiesService = propertiesService;
    }

    /**
     * Converts the uploaded properties file to a JSON string.
     *
     * @param file the properties file to convert.
     * @return the JSON representation of the properties.
     * @throws IOException if an error occurs while reading the properties file.
     */
    @PostMapping("/convert")
    @Operation(
            summary = "Converts a properties file to JSON",
            description = "Converts a properties file to JSON"
    )
    public String convertPropertiesToJson(
            @Parameter(
                    description = "The properties file to convert",
                    required = true
            )
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("Empty file");
        }

        String filename = file.getOriginalFilename();
        assert filename != null;
//        PropertiesFetcher propertiesFetcher = propertiesFetcherFactory.getPropertiesFetcher(filename);
        PropertiesFetcher propertiesFetcher = propertiesFetcherFactory.getPropertiesFetcher(file);

        Properties properties = propertiesFetcher.fetchProperties();

        PropertiesToJsonConverter converter = new PropertiesToJsonConverterImpl();
        String json = converter.convertToJson(filename, properties);

        return json;
    }

    @PostMapping("/convert-multiple")
    @Operation(
            summary = "Converts multiple properties files to JSON",
            description = "Converts multiple properties files to JSON"
    )
    public String convertMultiplePropertiesToJson(
            @Parameter(
                    description = "The properties files to convert",
                    required = true
            )
            @RequestParam("files") MultipartFile[] files) throws IOException {

        if (files == null || files.length == 0) {
            throw new IOException("No files provided");
        }

        log.info("Files: {}", files.length);
        //files
        for (MultipartFile file : files) {
            log.info("File: {}", file.getOriginalFilename());
        }

        Map<String, Properties> propertiesMap = new HashMap<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IOException("Empty file: " + file.getOriginalFilename());
            }

            String filename = file.getOriginalFilename();
            assert filename != null;
//            PropertiesFetcher propertiesFetcher = propertiesFetcherFactory.getPropertiesFetcher(filename);
            PropertiesFetcher propertiesFetcher = propertiesFetcherFactory.getPropertiesFetcher(file);

            // Fetch properties from the file using the appropriate PropertiesFetcher
            Properties properties = propertiesFetcher.fetchProperties();

            propertiesMap.put(filename, properties);
        }

        // Convert the Properties objects to a nested JSON structure
        PropertiesToJsonConverter converter = new PropertiesToJsonConverterImpl();
        return converter.convertAllToJson(propertiesMap);
    }

    @PostMapping("/convert-json")
    @Operation(
            summary = "Converts multiple properties files to JSON",
            description = "Converts multiple properties files to JSON"
    )
    public String convertJsonToYaml(
            @Parameter(
                    description = "The properties files to convert",
                    required = true
            )
            @RequestParam("files") MultipartFile file) throws IOException {

        File jsonFile = propertiesService.MultiPartToFile(file);

        return propertiesService.jsonToYaml(jsonFile);

    }

    @PostMapping("/convert-json-multiple")
    @Operation(
            summary = "Converts multiple properties files to JSON",
            description = "Converts multiple properties files to JSON"
    )
    public FileSystemResource convertJsonToYamlMultiple(
            @Parameter(
                    description = "The properties files to convert",
                    required = true
            )
            @RequestParam("files") MultipartFile file , @RequestParam String outputPath) throws IOException {
        File yamlFile = null;

        try {
            // Load the JSON file
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode jsonNode = jsonMapper.readTree(propertiesService.MultiPartToFile(file));

            if (jsonNode.isArray()) {
                // Iterate through the JSON array
                for (JsonNode object : jsonNode) {
                    // Create a new YAML ObjectMapper
                    ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

                    // Convert the JSON object to YAML
                    String yamlString = yamlMapper.writeValueAsString(object);

                    // Generate a unique YAML file name
                    String yamlFileName = "output_" + object.get("name").asText() + ".yaml";

                    // Write the YAML to a file
                    yamlFile = new File(outputPath + yamlFileName);
                    yamlMapper.writeValue(yamlFile, object);

                }
                return new FileSystemResource(yamlFile);

            } else {
                return new FileSystemResource(yamlFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new FileSystemResource(yamlFile);
        }


    }

}
