package com.incedoinc.configdataexchange.fetcher;

import com.incedoinc.configdataexchange.util.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class YamlFileFetcher implements PropertiesFetcher {

    private final String location;
    private final ResourceLoader resourceLoader;

    public YamlFileFetcher(String location, ResourceLoader resourceLoader) {
        this.location = location;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Properties fetchProperties() throws IOException {
        Resource resource = ResourceUtils.getResource(location, resourceLoader);
        log.debug("Resource filename: {}", resource.getFilename());  // Adjusted this line

        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(resource);
        return yamlFactory.getObject();
    }

    /**
     * Retrieves the name of the resource from which the properties are fetched.
     * <p>
     * In this implementation, the resource name is assumed to be the last part of the
     * <p>
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
