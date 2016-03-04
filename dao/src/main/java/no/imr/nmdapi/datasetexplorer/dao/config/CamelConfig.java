package no.imr.nmdapi.datasetexplorer.dao.config;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.ErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.routepolicy.quartz.CronScheduledRoutePolicy;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.camel.util.UnsafeUriCharactersEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Terry Hannant <a5119>
 */

@Configuration
public class CamelConfig extends SingleRouteCamelConfiguration{

    
    @Autowired
    @Qualifier("configuration")
    private org.apache.commons.configuration.Configuration config;

    
    @Override
    public RouteBuilder route() {
        
         return new RouteBuilder() {
            public void configure() {

                         
                onException(Exception.class)
                        .handled(true) 
                        .log(LoggingLevel.ERROR,"Error in routes");
                
                
                 from("quartz://cacheRefresh?cron="+UnsafeUriCharactersEncoder.encode(config.getString("cron.activation.time")))
                 .from("direct://reloadAll")
                 .from("timer://runOnce?repeatCount=1&delay=5000")
                            .to("bean:datasetDAO?method=updateDataset")
                            .to("bean:cruiseSeriesDAO?method=updateCruiseSeries")
                            .to("bean:surveyTimeSeriesDAO?method=updateTimeSeries")
                            .to("bean:cruiseDAO?method=updateCruise");
                         
                 
                 
            }
        };
    }
  
    
}
