package no.imr.nmdapi.datasetexplorer.dao.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Terry Hannant <a5119>
 */

@Configuration
public class CamelConfig extends SingleRouteCamelConfiguration{

    @Override
    public RouteBuilder route() {
        
         return new RouteBuilder() {
            public void configure() {
                 from("timer://updateCount?fixedRate=true&period=1800000").to("bean:datasetDAO?method=updateDataset");
            }
        };
    }
  
    
}
