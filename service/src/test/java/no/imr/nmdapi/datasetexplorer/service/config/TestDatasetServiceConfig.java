package no.imr.nmdapi.datasetexplorer.service.config;

import no.imr.nmdapi.datasetexplorer.service.config.DatasetExplorerServiceConfig;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author a5119
 */
public class TestDatasetServiceConfig {

    
    private DatasetExplorerServiceConfig config = new DatasetExplorerServiceConfig();

    @Test
    public void testGetNMDMissionQueryService() {
 
         assertNotNull(config.getDatasetService());
      }

}
