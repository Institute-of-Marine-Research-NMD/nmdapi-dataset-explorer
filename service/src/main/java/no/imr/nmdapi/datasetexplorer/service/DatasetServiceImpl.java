package no.imr.nmdapi.datasetexplorer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import no.imr.nmdapi.datasetexplorer.dao.DatasetDAO;
import no.imr.nmdapi.datasetexplorer.service.beans.ImportCount;
import no.imr.nmdapi.datasetexplorer.service.beans.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dataset service layer implementation.
 *
 * @author a5119
 */
public class DatasetServiceImpl implements DatasetService {

     
    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetService.class);

    @Autowired
    private DatasetDAO datasetDAO;
    
    @Override 
    public Collection listExistingDatasets(String missionType, String year, String platform,String delivery) {
        return     datasetDAO.listExistingDatasets(missionType,year,platform,delivery); 
    }

    
    @Override 
    public Collection listDeliveries(String missionType, String year, String platform) {
        LOGGER.debug("Start list deliveries");
       return   datasetDAO.listDeliveries(missionType, year, platform);
    }
    
    @Override 
    public Collection listPlatforms(String missionType, String year) {
        LOGGER.debug("Start list platform");
       return datasetDAO.listPlatforms(missionType, year);
    }
    
    
    @Override
    public Collection listYears(String missionType) {
        LOGGER.debug("Start list years");
        return datasetDAO.listYears(missionType);
    }

    @Override
    public Collection listMissionTypes() {
         LOGGER.debug("Start list mission types");
       return datasetDAO.listMissionTypes();
    }

    
    @Override
    public Collection summarizePlatformDataSets(String missionType, String year, String platform) {
      ArrayList <Level> result = new ArrayList<Level>();
        Level level;
        
       Collection<String> deliveryList = listDeliveries(missionType,year,platform);
       for (String delivery:deliveryList)
       {
             level = new Level("/"+missionType+"/"+year+"/"+platform+"/",delivery);
             level.setCount(countLoaded(missionType,year,platform,delivery));
             result.add(level);
       }
        
        return result;
    }
    
    @Override
    public Collection summarizeYearDataSets(String missionType, String year) {
        ArrayList <Level> result = new ArrayList<Level>();
        Level level;
        ArrayList<Level> deliveryList;
        ImportCount totalImportCount;
        
           
       Collection<String> platformList = listPlatforms(missionType,year);
       for (String platform:platformList)
       {
             totalImportCount = new ImportCount();
             deliveryList = (ArrayList <Level>) summarizePlatformDataSets(missionType,year,platform);
             for (Level deliveryLevel:deliveryList)
             {
               totalImportCount.add(deliveryLevel.getCount());
             }             
             
             level = new Level("/"+missionType+"/"+year+"/",platform);
             level.setCount(totalImportCount);
             result.add(level);
       }
        
        return result;
        
    }


    @Override
    public Collection summarizeMissionTypeDataSets(String missionType) {
        ArrayList <Level> result = new ArrayList<Level>();
        Level level;
        ArrayList<Level> platformList;
        ImportCount totalImportCount;
        
       Collection<String> yearList = listYears(missionType);
       for (String year:yearList)
       {
             totalImportCount = new ImportCount();
             platformList = (ArrayList <Level>) summarizeYearDataSets(missionType,year);
             for (Level platformLevel:platformList)
             {
               totalImportCount.add(platformLevel.getCount());
             }             
             
             level = new Level("/"+missionType+"/",year);
             level.setCount(totalImportCount);
             result.add(level);
       }
        
        return result;
    }

    @Override
    public Collection summarizeAllDataSets() {
        ArrayList <Level> result = new ArrayList<Level>();
        Level level;
        ArrayList<Level> yearList;
        ImportCount totalImportCount;
        
       Collection<String> missionTypeList = listMissionTypes();
       for (String missionType:missionTypeList)
       {
             totalImportCount = new ImportCount();
             yearList = (ArrayList <Level>) summarizeMissionTypeDataSets(missionType);
             for (Level yearLevel:yearList)
             {
               totalImportCount.add(yearLevel.getCount());
             }             
             
             level = new Level("/",missionType);
             level.setCount(totalImportCount);
             result.add(level);
       }
        
        return result;
    }

    @Override
    public Object countAllDataSets() {
        ArrayList<Level> missionTypeList;
        ImportCount totalImportCount = new ImportCount();

       missionTypeList = (ArrayList <Level>) summarizeAllDataSets();
             for (Level missionTypeLevel:missionTypeList)
             {
               totalImportCount.add(missionTypeLevel.getCount());
             }             
        return totalImportCount;
    }
     
   public boolean checkDataSetLoaded(String missionType, String year, String platform, String delivery,String dataType)
      {
       return datasetDAO.checkDataSetLoaded(missionType, year, platform, delivery, dataType) ;
      }


  
   public ImportCount countLoaded(final String missionType, final String year, final String platform, final String delivery) 
     {
         ImportCount result = new ImportCount();
         
        ArrayList<String> datasetList = (ArrayList<String>) listExistingDatasets(missionType, year, platform,  delivery);
          result.incMissionCount();
        for (String dataset:datasetList)
        {
             result.incTotal();
             if (checkDataSetLoaded(missionType,year,platform,delivery,dataset))
             {
                  result.incActual();
             }
        }
         return result;
     }   

    @Override
    public Map listExistingDatasetsDetail(String missionType, String year, String platform, String delivery) {
        return datasetDAO.listExistingDatasetsDetail(missionType, year, platform, delivery);
    }
 
    
}
