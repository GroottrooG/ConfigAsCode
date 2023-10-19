package com.incedoinc.configdataexchange.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Implementation of the {@link PropertiesToJsonConverter} interface.
 * <p>
 * This class provides functionality to convert a {@link Properties} object
 * <p>
 * into a JSON string with a specified source, and nests the properties under a "value" field.
 */
public class PropertiesToJsonConverterImpl implements PropertiesToJsonConverter {
    /**
     * Converts the given {@link Properties} object into a JSON string.
     * <p>
     * The resulting JSON will have a "source" field with the provided source value,
     * <p>
     * and a "value" field containing the properties as a nested JSON object.
     *
     * @param source     the source of the properties, to be included in the resulting JSON.
     * @param properties the {@link Properties} object to convert.
     * @return a JSON string representation of the given properties.
     * @throws IOException if an I/O error occurs during conversion.
     */
    @Override
    public String convertToJson(String source, Properties properties) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode valueNode = objectMapper.createObjectNode();  // Create a new node for "value"

        for (String key : properties.stringPropertyNames()) {
            String[] parts = key.split("[file://.]\\.");
            ObjectNode currentNode = valueNode;  // Start with valueNode instead of rootNode
            for (int i = 0; i < parts.length; i++) {
                if (i == parts.length - 1) {
                    currentNode.put(parts[i], properties.getProperty(key));
                } else {
                    JsonNode node = currentNode.get(parts[i]);
                    if (node == null || !node.isObject()) {
                        currentNode.set(parts[i], objectMapper.createObjectNode());
                    }
                    currentNode = (ObjectNode) currentNode.get(parts[i]);
                }
            }
        }

        rootNode.put("source", source);  // Set the source field in the root node
        rootNode.set("value", valueNode);  // Set the value field in the root node

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

    @Override
    public String convertAllToJson(Map<String, Properties> propertiesMap) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        for (Map.Entry<String, Properties> entry : propertiesMap.entrySet()) {
            String source = entry.getKey();
            Properties properties = entry.getValue();

            // Convert individual Properties object to a JSON node
            String json = convertToJson(source, properties);
            JsonNode jsonNode = objectMapper.readTree(json);

            // Extract the "value" node from the JSON and add it to the root node
            JsonNode valueNode = jsonNode.get("value");
            rootNode.set(source, valueNode);
        }

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
    }

}
