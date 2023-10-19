package com.incedoinc.configdataexchange.fetcher;

import java.io.IOException;
import java.util.Properties;

/**
 * Interface for fetching properties from different sources.
 */
public interface PropertiesFetcher {

    /**
     * Fetches properties from a specified source.
     *
     * @return the fetched properties.
     * @throws IOException if an I/O error occurs during fetching.
     */
    Properties fetchProperties() throws IOException;

    /**
     * Retrieves the name of the resource from which the properties are fetched.
     * <p>
     * The exact format of the resource name is implementation-dependent.
     *
     * @return the name of the resource.
     */
    String getResourceName();
}