package com.incedoinc.configdataexchange.converter;


import java.io.IOException;
import java.util.Map;
import java.util.Properties;


/**
 * Interface for converting a {@link Properties} object to a JSON string.
 * <p>
 * Implementations of this interface should be able to convert a given
 * <p>
 * {@link Properties} object into a JSON string, and include a specified
 * <p>
 * source in the resulting JSON structure.
 */

public interface PropertiesToJsonConverter {


    /**
     * Converts the given {@link Properties} object into a JSON string.
     * <p>
     * The resulting JSON will have a "source" field with the provided
     * <p>
     * source value, and a "value" field containing the properties as
     * <p>
     * a nested JSON object.
     *
     * @param source     the source of the properties, to be included
     *                   <p>
     *                   in the resulting JSON.
     * @param properties the {@link Properties} object to convert.
     * @return a JSON string representation of the given properties.
     * @throws IOException if an I/O error occurs during conversion.
     */

    String convertToJson(String source, Properties properties) throws IOException;

    String convertAllToJson(Map<String, Properties> propertiesMap) throws IOException;


}


