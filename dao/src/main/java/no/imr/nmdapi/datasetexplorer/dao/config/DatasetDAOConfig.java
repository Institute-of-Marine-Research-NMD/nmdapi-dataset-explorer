package no.imr.nmdapi.datasetexplorer.dao.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import no.imr.nmdapi.common.jaxb.converters.JAXBHttpMessageConverter;
import no.imr.nmdapi.datasetexplorer.dao.CruiseDAOTaskImpl;
import no.imr.nmdapi.datasetexplorer.dao.CruiseSeriesDAO;
import no.imr.nmdapi.datasetexplorer.dao.CruiseSeriesDAOTaskImpl;
import no.imr.nmdapi.datasetexplorer.dao.DatasetDAO;
import no.imr.nmdapi.datasetexplorer.dao.DatasetDAOTaskImpl;
import no.imr.nmdapi.datasetexplorer.dao.SurveyTimeSeriesDAO;
import no.imr.nmdapi.datasetexplorer.dao.SurveyTimeSeriesDAOTaskImpl;
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
     * @return A reference service implementation.
     */
    @Bean(name = "datasetDAO")
    public DatasetDAO getDatasetDAO() {
        return new DatasetDAOTaskImpl();
    }

     /**
     * Creates the service implementation.
     *
     * @return A reference service implementation.
     */
    @Bean(name = "cruiseSeriesDAO")
    public CruiseSeriesDAO getCruiseSeriesDAO() {
        return new CruiseSeriesDAOTaskImpl();
    }
  
       /**
     * Creates the service implementation.
     *
     * @return A reference service implementation.
     */
    @Bean(name = "surveyTimeSeriesDAO")
     public SurveyTimeSeriesDAO getSurveyTimeSeriesDAO() {
        return new SurveyTimeSeriesDAOTaskImpl();
    }
  
    
   /**
     * Creates the service implementation.
     *
     * @return A reference service implementation.
     */
    @Bean(name = "cruiseDAO")
    public CruiseDAOTaskImpl getCruiseDAO() {
        return new CruiseDAOTaskImpl();
    }

    
    /**
     * Creates the restOpertaion implementation.
     *
     * @return A RestOperation implementation.
     */
    @Bean(name = "restClient")
    public RestOperations getRestOperations() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getMessageConverters());
        return restTemplate;
    }

    /**
     * Creates the restOpertaion implementation for HEAD requests.
     *
     * @return A RestOperation implementation.
     */
    @Bean(name = "restHeadClient")
    public RestOperations getRestHeadOperations() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                return (!(statusCode == HttpStatus.NOT_FOUND || statusCode.is2xxSuccessful()));
            }
        });
        return restTemplate;
    }

    @Bean(name = "datasetUnMarshaller")
    public Unmarshaller getMissionUnMarshaller() {
        Unmarshaller unMarshaller = null;

        JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance("no.imr.nmd.commons.dataset.jaxb");
            unMarshaller = ctx.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Can not create Dataset unmarshaller ", ex);
        }
        return unMarshaller;
    }
    
    @Bean(name = "cruiseUnMarshaller")
    public Unmarshaller getCruiseUnMarshaller() {
        Unmarshaller unMarshaller = null;

        JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance("no.imr.nmd.commons.cruise.jaxb");
            unMarshaller = ctx.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Can not create Cruise unmarshaller", ex);
        }
        return unMarshaller;
    }
    
    @Bean(name = "cruiseseriesUnMarshaller")
    public Unmarshaller getCruiseseriesUnMarshaller() {
        Unmarshaller unMarshaller = null;

        JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance("no.imr.nmd.commons.cruiseseries.domain.v1");
            unMarshaller = ctx.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Can not create Cruise unmarshaller ", ex);
        }
        return unMarshaller;
    }

    
    @Bean(name = "surveytimeseriesUnMarshaller")
    public Unmarshaller getSurveyTimeseriesUnMarshaller() {
        Unmarshaller unMarshaller = null;

        JAXBContext ctx;
        try {
            ctx = JAXBContext.newInstance("no.imr.nmd.commons.surveytimeseries.jaxb");
            unMarshaller = ctx.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error("Can not create Survey time  unmarshaller ", ex);
        }
        return unMarshaller;
    }

    

    
    
   

    /**
     * Create messages converters to be used * @return
     */
    private List<HttpMessageConverter<?>> getMessageConverters() {

        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setPrettyPrint(true);
        jsonConverter.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        converters.add(jsonConverter);

        Jaxb2RootElementHttpMessageConverter xmlConvertor = new Jaxb2RootElementHttpMessageConverter();

        converters.add(xmlConvertor);

        JAXBHttpMessageConverter missionConverter;
        try {
            missionConverter = new JAXBHttpMessageConverter(new MissionNamespacePrefixMapper(),false,
                    "no.imr.nmdapi.generic.response.v1");
            converters.add(missionConverter);

        } catch (JAXBException ex) {
            LOG.error("Can not create Mission message converter ", ex);
        }

        return converters;

    }

}
