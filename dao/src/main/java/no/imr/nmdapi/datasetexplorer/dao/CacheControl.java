package no.imr.nmdapi.datasetexplorer.dao;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class CacheControl {
  @EndpointInject(uri="direct:reloadAll")
  ProducerTemplate producer;
 
   public void refreshAll() {
       producer.sendBody("");
   }
          
  
  
    
}
