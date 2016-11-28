package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import no.imr.nmdapi.datasetexplorer.service.beans.CruiseDatasetStatus;

/**
 * Service API for dataset explorer
 *
 * @author a5119
 */
public interface DatasetService {

    
     Collection listExistingDatasets(String missionType, String year, String platform, String delivery);

    Map listExistingDatasetsDetail(String missionType, String year, String platform, String delivery);

    
    Collection listDeliveries(String missionType, String year, String platform) ;
    
    Collection listPlatforms(String missionType, String year) ;

    Collection listYears(String missionType) ;

   Collection listMissionTypes() ;

    Collection summarizePlatformDataSets(String missionType, String year, String platform);

    Collection summarizeYearDataSets(String missionType, String year) ;
    
    Collection summarizeMissionTypeDataSets(String missionType) ;

    Collection summarizeAllDataSets() ;
    
    Map summarizeByCruise(String missionType, String year);
    
    List summarizeDatasetsStatus(String missionType, String year);
    
    CruiseDatasetStatus getCruiseStatus(String missionType, String year, String platform, String delivery);
    
    CruiseDatasetStatus getCruiseStatus(String cruisePath) ;
    
    List listDatasets(String missionType, String year, String platform, String delivery,String datatype,
                            String cruiseNumber,String status);
    
    Collection listCruiseDatasetTypes();
            
    Object countAllDataSets();

}
