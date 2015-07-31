package no.imr.nmdapi.datasetexplorer.service.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import no.imr.nmdapi.common.converters.JAXBHttpMessageConverter;
import no.imr.nmdapi.datasetexplorer.service.DatasetService;
import no.imr.nmdapi.datasetexplorer.service.DatasetServiceImpl;
import no.imr.nmdapi.datasetexplorer.service.mapper.MissionNamespacePrefixMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;



/**
 * This contains all configuration for the dataset client services.
 *
 * @author a5119
 */
@Configuration
public class DatasetServiceConfig {

    
    
    /**
     * Creates the service implementation.
     *
     * @return  A reference service implementation.
     */
    @Bean(name="datasetService")
    public DatasetService getDatasetService() {
          return new DatasetServiceImpl();
    }
 
   
    
    /**
     * Create messages converters to be used
     * *
     * @return 
     */
    private List<HttpMessageConverter<?>> getMessageConverters() {
        
    List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    jsonConverter.setPrettyPrint(true);
    jsonConverter.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    converters.add(jsonConverter);
    
    Jaxb2RootElementHttpMessageConverter  xmlConvertor = new Jaxb2RootElementHttpMessageConverter();
   converters.add(xmlConvertor);
  
   JAXBHttpMessageConverter missionConverter;
        try {
            missionConverter = new JAXBHttpMessageConverter(new MissionNamespacePrefixMapper(),
                    "no.imr.nmdapi.generic.response.v1");
               converters.add(missionConverter);

        } catch (JAXBException ex) {
            Logger.getLogger(DatasetServiceConfig.class.getName()).log(Level.SEVERE, null, ex);
        }

   
    return converters;

    }
   
    

      
}
