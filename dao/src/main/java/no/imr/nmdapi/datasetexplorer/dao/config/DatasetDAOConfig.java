package no.imr.nmdapi.datasetexplorer.dao.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import no.imr.nmdapi.common.converters.JAXBHttpMessageConverter;
import no.imr.nmdapi.datasetexplorer.dao.DatasetDAO;
import no.imr.nmdapi.datasetexplorer.dao.DatasetDAOFileImpl;
import no.imr.nmdapi.datasetexplorer.dao.DatasetDAORestImpl;


import no.imr.nmdapi.datasetexplorer.dao.mapper.MissionNamespacePrefixMapper;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;

import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;


/**
 * This contains all configuration for the rest client services.
 *
 * @author a5119
 */
@Configuration
public class DatasetDAOConfig {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DatasetDAOConfig.class);
    
    
    /**
     * Creates the service implementation.
     *
     * @return  A reference service implementation.
     */
    @Bean(name="datasetDAO")
    public DatasetDAO getDatasetDAO() {
        return new DatasetDAOFileImpl();
    }
 
    
    /**
     * Creates the restOpertaion implementation.
     *
     * @return  A RestOperation  implementation.
     */
    @Bean(name="restClient")
    public RestOperations getRestOperations() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getMessageConverters());
        return restTemplate;
    }
   
   /**
     * Creates the restOpertaion implementation for HEAD requests.
     *
     * @return  A RestOperation  implementation.
     */
    @Bean(name="restHeadClient")
    public RestOperations getRestHeadOperations() {
        RestTemplate restTemplate = new RestTemplate();
         restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            protected boolean hasError(HttpStatus statusCode) {
              return (!(statusCode == HttpStatus.NOT_FOUND || statusCode.is2xxSuccessful()));
           }});
        return restTemplate;
    }
   
    @Bean(name="missionUnmarshaller")
    public Unmarshaller getMissionUnMarshaller()
    {
            Unmarshaller unMarshaller = null ;
     
         JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance("no.imr.nmdapi.generic.nmdmission.domain.v1");
              unMarshaller = ctx.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Can not create Mission unmarshaller message converter ", ex);
        }
     return unMarshaller;
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
            LOG.error("Can not create Mission message converter ", ex);
        }

    return converters;

    }
    
    
      
}
