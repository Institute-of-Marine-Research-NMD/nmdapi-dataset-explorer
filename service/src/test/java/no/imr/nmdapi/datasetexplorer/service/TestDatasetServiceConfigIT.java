package no.imr.nmdapi.datasetexplorer.service;

import no.imr.nmdapi.datasetexplorer.service.DatasetService;
import java.util.Collection;
import java.util.Map;
import no.imr.nmdapi.datasetexplorer.dao.config.DatasetDAOConfig;
import no.imr.nmdapi.generic.response.v1.ResultElementType;
import no.imr.nmdapi.datasetexplorer.service.beans.Level;
import no.imr.nmdapi.datasetexplorer.service.config.DatasetExplorerServiceConfig;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author a5119
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class,
        classes = {DatasetExplorerServiceConfig.class,DatasetDAOConfig.class,TestContextConfig.class })

public class TestDatasetServiceConfigIT {

    @Autowired private DatasetService datasetService;
  
      @Test
    public void testDummy() {
        
      } 
  
    /*
 
    @Test
    public void testListDeliveries() {
        
        Collection deliveryList = datasetService.listDeliveries("Forskningsfartøy","2015","GOSars-LMEL");
        assertNotNull(deliveryList);
        for (Object delivery:deliveryList)
        {
            assertNotNull(delivery);
            assertTrue(delivery  instanceof  String);
        }
       
      } 
    
    @Test
    public void testListPlatforms() {
        
        Collection platformList = datasetService.listPlatforms("Forskningsfartøy","2015");
        assertNotNull(platformList);
        for (Object platform:platformList)
        {
            assertNotNull(platform);
            assertTrue(platform  instanceof  String);
        }
      }

    @Test
    public void testListYears() {
        
        Collection yearList = datasetService.listYears("Forskningsfartøy");
        assertNotNull(yearList);
        for (Object year:yearList)
        {
            assertNotNull(year);
            assertTrue(year  instanceof  String);
        }
      }

    @Test
    public void testListMissionType() {
   
        Collection missionTypeList = datasetService.listMissionTypes();
        assertNotNull(missionTypeList);
        for (Object missionType:missionTypeList)
        {
            assertNotNull(missionType);
            assertTrue(missionType  instanceof  String);
        }
      }
    
    @Test
    public void testSummarizePlatform() {
        
        Collection levelList = datasetService.summarizePlatformDataSets("Forskningsfartøy","2015","GOSars-LMEL");
        assertNotNull(levelList);
        for (Object level:levelList)
        {
            assertNotNull(level);
            assertTrue(level  instanceof  Level);
           assertNotNull( ((Level)level).getName());
           assertNotNull( ((Level)level).getParentPath());
           assertNotNull( ((Level)level).getCount());
        }
       
      } 

    @Test
    public void testSummarizeYear() {
        
        Collection levelList = datasetService.summarizeYearDataSets("Forskningsfartøy","2015");
        assertNotNull(levelList);
        for (Object level:levelList)
        {
            assertNotNull(level);
            assertTrue(level  instanceof  Level);
           assertNotNull( ((Level)level).getName());
           assertNotNull( ((Level)level).getParentPath());
           assertNotNull( ((Level)level).getCount());
        }
       
      }
    
    @Test
    public void testSummarizeMissiontType() {
        
        Collection levelList = datasetService.summarizeMissionTypeDataSets("Forskningsfartøy");
        assertNotNull(levelList);
        for (Object level:levelList)
        {
            assertNotNull(level);
            assertTrue(level  instanceof  Level);
           assertNotNull( ((Level)level).getName());
           assertNotNull( ((Level)level).getParentPath());
           assertNotNull( ((Level)level).getCount());
        }
       
      } 

    @Test
    public void testSummarizeAll() {
        
        Collection levelList = datasetService.summarizeAllDataSets();
        assertNotNull(levelList);
        for (Object level:levelList)
        {
            assertNotNull(level);
            assertTrue(level  instanceof  Level);
           assertNotNull( ((Level)level).getName());
           assertNotNull( ((Level)level).getParentPath());
           assertNotNull( ((Level)level).getCount());
        }
       
      } 

    @Test
    public void testCountAll() {
       Object count  = datasetService.countAllDataSets();
        assertNotNull(count);
      } 

  @Test
    public void testListExistingDataSets() {
        
        Collection dataTypeList = datasetService.listExistingDatasets("Forskningsfartøy","2015","GOSars-LMEL","2015101");
        assertNotNull(dataTypeList);
        for (Object dataType:dataTypeList)
        {
            assertNotNull(dataType);
           assertTrue(dataType  instanceof  String);
        }
       
      }  
    
    
    @Test
    public void testListDataSetsDetail() {
        
        Map datasets = datasetService.listExistingDatasetsDetail("Forskningsfartøy","2015","GOSars-LMEL","2015101");
        assertNotNull(datasets);
        for (Object dataType:datasets.keySet())
        {
            assertNotNull(dataType);
           assertTrue(dataType  instanceof  String);
           assertNotNull(datasets.get(dataType));
           assertTrue(datasets.get(dataType) instanceof  String);
                      
        }
       
      } 
*/
    
}
