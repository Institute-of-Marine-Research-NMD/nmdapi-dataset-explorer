
package no.imr.nmdapi.datasetexplorer.dao;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Terry Hannant <a5119>
 */
@Configuration
public class TestContextConfig {
     
        @Bean
        public org.apache.commons.configuration.Configuration configuration() {
            org.apache.commons.configuration.Configuration configuration = mock(org.apache.commons.configuration.Configuration.class);
         //   doReturn("http://tomcat7-test.imr.no:8080/apis/").when(configuration).getString("base.URL");
          doReturn("http://localhost:8803/apis/").when(configuration).getString("base.URL");
          doReturn("/app/sea2dataloader/loader/").when(configuration).getString("base.filePath");

            return configuration;
        }    
    
}
