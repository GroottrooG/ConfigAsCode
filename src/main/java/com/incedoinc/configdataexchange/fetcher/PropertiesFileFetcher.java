package com.incedoinc.configdataexchange.fetcher;

import com.incedoinc.configdataexchange.util.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;
/**
 * Fetches properties from a local file.
 */

/**
 * Fetches properties from a file, which can be located either on the classpath
 * or in the local filesystem.
 */

@Slf4j

//@Component

public class PropertiesFileFetcher implements PropertiesFetcher {
    private final String location;

    private final ResourceLoader resourceLoader;

    /**
     * Constructor.
     *
     * @param location       the location of the properties file. Can be a classpath
     *                       <p>
     *                       location (e.g., "classpath:application.properties") or a
     *                       <p>
     *                       local file path (e.g., "file:/etc/application.properties").
     * @param resourceLoader the {@link ResourceLoader} to use for loading the file.
     */

    public PropertiesFileFetcher(String location, ResourceLoader resourceLoader) {

        this.location = location;

        this.resourceLoader = resourceLoader;

    }

    @Override

    public Properties fetchProperties() throws IOException {

        Resource resource = ResourceUtils.getResource(location, resourceLoader);

        log.debug("Resource filename: {}", resource.getFilename()); // Adjusted this line

        return PropertiesLoaderUtils.loadProperties(resource);

    }

    /**
     * Retrieves the name of the resource from which the properties are fetched.
     * In this implementation, the resource name is assumed to be the last part of the
     * location path, with path segments separated by "/".
     *
     * @return the name of the resource, extracted from the location path.
     */

    @Override

    public String getResourceName() {

        // Assuming the resource name is the last part of the location path

        String[] parts = location.split("/");

        return parts[parts.length - 1];

    }

}
