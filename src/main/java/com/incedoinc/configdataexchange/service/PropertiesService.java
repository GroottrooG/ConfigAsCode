package com.incedoinc.configdataexchange.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

@Service
public class PropertiesService {

    public File MultiPartToFile(MultipartFile file){
        File jsonFile = new File("C:\\Users\\nizam\\Downloads\\configdataexchange\\configdataexchange\\src\\main\\resources\\configs\\" + file.getOriginalFilename());

        try (FileOutputStream fos = new FileOutputStream(jsonFile)) {
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jsonFile;
    }


    public String jsonToYaml(File jsonFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        String outputYamlFilePath = "C:\\Users\\nizam\\Downloads\\configdataexchange\\configdataexchange\\src\\main\\resources\\configs\\output.yaml";

        try {

            // Read the JSON data from the input file
            Object json = objectMapper.readValue(jsonFile, Object.class);

            // Initialize SnakeYAML Yaml with appropriate options
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            // Write the JSON data to the output YAML file
            try (FileWriter writer = new FileWriter(outputYamlFilePath)) {
                yaml.dump(json, writer);
                return "YAML file created successfully at " + outputYamlFilePath;

            } catch (IOException e) {
                return "Error creating the YAML file: " + e.getMessage();
            }
        } catch (IOException e) {
            return "Error reading the JSON file: " + e.getMessage();
        }
    }


}
