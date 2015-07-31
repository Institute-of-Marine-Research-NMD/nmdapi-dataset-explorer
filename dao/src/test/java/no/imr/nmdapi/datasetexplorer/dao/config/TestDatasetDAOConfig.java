package no.imr.nmdapi.datasetexplorer.dao.config;

import no.imr.nmdapi.datasetexplorer.dao.config.DatasetDAOConfig;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author a5119
 */
public class TestDatasetDAOConfig {

    
    private DatasetDAOConfig config = new DatasetDAOConfig();

    @Test
    public void testGetDatasetDAO() {
 
         assertNotNull(config.getDatasetDAO());
      }

}
