package no.imr.nmdapi.datasetexplorer.dao;

import java.util.Collection;
import no.imr.nmdapi.datasetexplorer.dao.config.DatasetDAOConfig;
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
        classes = {DatasetDAOConfig.class,TestContextConfig.class })

public class TestNMDDataSetDAOConfigIT {

    @Autowired private DatasetDAO datasetDao;
  
      @Test
    public void testDummy() {
        
      } 
  
    //TODO add not found tests
    
    @Test
    public void testListDeliveries() {
        
        Collection deliveryList = datasetDao.listDeliveries("Forskningsfartøy","2014","G O Sars-LMEL");
        assertNotNull(deliveryList);
        for (Object delivery:deliveryList)
        {
            assertNotNull(delivery);
            assertTrue(delivery  instanceof  String);
        }
       
      } 
    
    
    @Test
    public void testListExistingDataSets() {
       Collection dataTypeList = datasetDao.listExistingDatasets("Forskningsfartøy","2015","G O Sars-LMEL","2015101");
        assertNotNull(dataTypeList);
        for (Object dataType:dataTypeList)
        {
           System.out.println(dataType);
            
            assertNotNull(dataType);
           assertTrue(dataType  instanceof  String);
        }
       
      }  
    
    @Test
    public void testListPlatforms() {
        
        Collection platformList = datasetDao.listPlatforms("Forskningsfartøy","2015");
        assertNotNull(platformList);
        for (Object platform:platformList)
        {
            assertNotNull(platform);
            assertTrue(platform  instanceof  String);
        }
      }

    //@Test
    public void testListYears() {
        
        Collection yearList = datasetDao.listYears("Forskningsfartøy");
        assertNotNull(yearList);
        for (Object year:yearList)
        {
            assertNotNull(year);
            assertTrue(year  instanceof  String);
        }
      }

    @Test
    public void testListMissionType() {
   
        Collection missionTypeList = datasetDao.listMissionTypes();
        assertNotNull(missionTypeList);
        for (Object missionType:missionTypeList)
        {
            assertNotNull(missionType);
            assertTrue(missionType  instanceof  String);
        }
      }
    
    
    /*
    @Test
    public void testSummarizePlatform() {
        
        Collection levelList = datasetDao.summarizePlatformDataSets("Forskningsfartøy","2015","G O Sars-LMEL");
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
        
        Collection levelList = datasetDao.summarizeYearDataSets("Forskningsfartøy","2015");
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
        
        Collection levelList = datasetDao.summarizeMissionTypeDataSets("Forskningsfartøy");
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
        
        Collection levelList = datasetDao.summarizeAllDataSets();
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
       Object count  = datasetDao.countAllDataSets();
        assertNotNull(count);
      } 
     
    
    @Test
    public void testListDataSetsDetail() {
        
        Map datasets = datasetDao.listExistingDatasetsDetail("Forskningsfartøy","2015","G O Sars-LMEL","2015101");
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
