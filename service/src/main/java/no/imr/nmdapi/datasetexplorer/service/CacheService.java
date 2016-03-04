package no.imr.nmdapi.datasetexplorer.service;

import no.imr.nmdapi.datasetexplorer.dao.CacheControl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class CacheService {

      @Autowired CacheControl cacheControl;
      
      
      public void refreshAll() {
          cacheControl.refreshAll();
      }
     
}
