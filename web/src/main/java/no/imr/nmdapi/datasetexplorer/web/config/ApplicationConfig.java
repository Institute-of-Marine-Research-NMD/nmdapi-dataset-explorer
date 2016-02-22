package no.imr.nmdapi.datasetexplorer.web.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.reloading.ReloadingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author a5119
 *
 *
 */
@Configuration
public class ApplicationConfig {

    /**
     * Configuration object for communicating with property data.
     *
     * @return Configuration object containing properties.
     * @throws ConfigurationException Error during instantiation.
     */
    @Bean
    public org.apache.commons.configuration.Configuration configuration() throws ConfigurationException {
        org.apache.commons.configuration.PropertiesConfiguration configuration = new org.apache.commons.configuration.PropertiesConfiguration(System.getProperty("catalina.base") + "/conf/dataset_explorer_v1.properties");
        ReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
        configuration.setReloadingStrategy(reloadingStrategy);
        return configuration;
    }
    
    
}
