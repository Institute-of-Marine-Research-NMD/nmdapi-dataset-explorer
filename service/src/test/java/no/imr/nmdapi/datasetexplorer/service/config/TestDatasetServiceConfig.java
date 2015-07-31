package no.imr.nmdapi.datasetexplorer.service.config;

import no.imr.nmdapi.datasetexplorer.service.config.DatasetServiceConfig;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author a5119
 */
public class TestDatasetServiceConfig {

    
    private DatasetServiceConfig config = new DatasetServiceConfig();

    @Test
    public void testGetNMDMissionQueryService() {
 
         assertNotNull(config.getDatasetService());
      }

}
