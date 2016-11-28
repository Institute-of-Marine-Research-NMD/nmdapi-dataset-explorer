package no.imr.nmdapi.datasetexplorer.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import no.imr.nmd.commons.dataset.jaxb.DatasetType;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface DatasetDAO {
    
    /**
     * List deliveries for platform
     * 
     * @param missionType
     * @param year
     * @param platform
     * @return 
     */
  public Collection listDeliveries(String missionType, String year, String platform) ;
 
  /**
   * List platforms for mission type and year
   * @param missionType
   * @param year
   * @return 
   */
  public Collection listPlatforms(String missionType, String year) ;
 
  /**
   * List years for mission  type
   * @param missionType
   * @return 
   */
   public Collection listYears(String missionType);

   /**
    * List all mission types
    * @return 
    */
    public Collection listMissionTypes() ;
    
 
     
   /**
     * Get collection of existing datasets
     * 
     * @param missionType
     * @param year
     * @param platform
     * @param delivery
     * @return 
     */
     public Collection listExistingDatasets(String missionType, String year, String platform, String delivery);
 
     /**
      * List dataset detail 
      * @param missionType
      * @param year
      * @param platform
      * @param delivery
      * @return 
      */
     public Map listExistingDatasetsDetail(String missionType, String year, String platform, String delivery) ;

     /**
      * Check if a data set file exists
      * @param missionType
      * @param year
      * @param platform
      * @param delivery
      * @param dataType
      * @return 
      */
     public  boolean checkDatasetFileExists(String missionType, String year, String platform, String delivery,String dataType);
 
     
     public DatasetType getDatasetDetail(String path);
     
     public List listDatasetNames();
     
}
