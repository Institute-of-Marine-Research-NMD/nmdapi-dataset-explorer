package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;
import java.util.Map;

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
    
    Object countAllDataSets();

}
