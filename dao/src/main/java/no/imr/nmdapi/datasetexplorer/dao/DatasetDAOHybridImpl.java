package no.imr.nmdapi.datasetexplorer.dao;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import no.imr.nmd.commons.cruise.jaxb.CruiseType;
import no.imr.nmd.commons.cruise.jaxb.DataTypeEnum;
import no.imr.nmd.commons.cruise.jaxb.DatasetType;
import no.imr.nmd.commons.cruise.jaxb.ExistsEnum;
import no.imr.nmdapi.exceptions.NotFoundException;
import no.imr.nmdapi.generic.response.v1.ListElementType;
import no.imr.nmdapi.generic.response.v1.ResultElementType;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriTemplate;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class DatasetDAOHybridImpl implements DatasetDAO {
    
    private static final Logger LOG = LoggerFactory.getLogger(DatasetDAOHybridImpl.class);
     
    @Autowired
    private RestOperations restClient;
    @Autowired
    private RestOperations restHeadClient;

      @Autowired
    private Unmarshaller missionUnMarshaller;

    @Autowired 
     private Configuration config;
    
    //config info
    String deliveryPath ="/nmdapi/nmdmission/v1/{missionType}/{year}/{platform}/{delivery}";
    String platformPath ="/nmdapi/nmdmission/v1/{missionType}/{year}/{platform}";
    String yearPath ="/nmdapi/nmdmission/v1/{missionType}/{year}";
    String missionTypePath ="/nmdapi/nmdmission/v1/{missionType}";
    String rootMissionPath ="/nmdapi/nmdmission/v1";
    
    String datasetDataPath = "/nmdapi/nmd{dataType}/v1/{missionType}/{year}/{platform}/{delivery}";

    
        private String expand(String separator, String... args) {
        StringBuilder result = new StringBuilder();
        for (String arg : args) {
              result.append(arg);
              result.append(separator);
        }
        return result.toString();
    }
        
    @Override
    public Collection listExistingDatasets(String missionType, String year, String platform, String delivery) {
        String filePath = config.getString("base.filePath") + expand(File.separator, missionType, year, platform, delivery) + "/cruise/data.xml";
        File path = new File(filePath);

        ArrayList<String> result = new ArrayList<String>();
       CruiseType mission;
        try {
            mission = (CruiseType) missionUnMarshaller.unmarshal(path);
            result = new ArrayList<String>();
            for (DatasetType missionDetail : mission.getDatasets().getDataset()) {
                if (missionDetail.getCollected().equals(ExistsEnum.YES)) {
                    result.add(missionDetail.getDataType().name());
                }
            }
        } catch (JAXBException ex) {
            new NotFoundException(filePath + " not found ."); //TODO handle this better
        }
        return result;
    }
    
      
        
    @Override 
    public Collection listDeliveries(String missionType, String year, String platform) {
       String endPointURL = config.getString("base.URL")+platformPath;
      ListElementType typeList;
      try {
        typeList  = restClient.getForObject(endPointURL,ListElementType.class, missionType,year,platform);
      } catch (HttpClientErrorException ex)   {
          //TODO expand to check status and throw appropriate excepetion
            throw new NotFoundException( endPointURL +  " not found .");
      }
       return simplifyList(typeList.getElement());
    }
    
    @Override 
    public Collection listPlatforms(String missionType, String year) {
        String endPointURL = config.getString("base.URL")+yearPath;
        ListElementType typeList ;
        
      try {
              typeList = restClient.getForObject(endPointURL,ListElementType.class, missionType,year);
      } catch (HttpClientErrorException ex)   {
          //TODO expand to check status and throw appropriate excepetion
            throw new NotFoundException( endPointURL +  " not found .");
      }
       return simplifyList(typeList.getElement());
    }
    
    
    @Override
    public Collection listYears(String missionType) {
       String endPointURL = config.getString("base.URL")+missionTypePath;
       ListElementType typeList;
        try {
          typeList = restClient.getForObject(endPointURL,ListElementType.class, missionType);
     } catch (HttpClientErrorException ex)   {
          //TODO expand to check status and throw appropriate excepetion
            throw new NotFoundException( endPointURL +  " not found . "+ex.getStatusText());
      }

       return simplifyList(typeList.getElement());
    }

    @Override
    public Collection listMissionTypes() {
        String endPointURL = config.getString("base.URL")+rootMissionPath;
        ListElementType typeList;
       try {
          typeList  = restClient.getForObject(endPointURL,ListElementType.class);
     } catch (HttpClientErrorException ex)   {
          //TODO expand to check status and throw appropriate excepetion
            throw new NotFoundException( endPointURL +  " not found .");
      }

       return simplifyList(typeList.getElement());
    }
   
   

   
     
   @Override
    public Map listExistingDatasetsDetail(String missionType, String year, String platform, String delivery) {
        
    ArrayList<String> dataSets =  (ArrayList<String>) listExistingDatasets(missionType,year,platform,delivery);
    String dataURL;
    HashMap<String,String> result = new HashMap();
    
     UriTemplate deliveryPathTemplate = new UriTemplate(config.getString("base.URL")+deliveryPath);
     UriTemplate datasetPathTemplate = new UriTemplate(config.getString("base.URL")+datasetDataPath);
  
     result.put("metadata",deliveryPathTemplate.expand(missionType,year,platform,delivery).toString());

                for (String dataType:dataSets)
                {
                              if (checkDatasetFileExists(missionType,year,platform,delivery,dataType))    {
                                            dataURL =datasetPathTemplate.expand(dataType, missionType,year,platform,delivery).toString();
                                       }
                              else {
                               dataURL ="N/A";
                              } 
                    result.put(dataType,dataURL);
                }
        return result;
        
    }
    
   @Override
     public  boolean checkDatasetFileExists(String missionType, String year, String platform, String delivery,String dataType)
      {
        String endPointURL = config.getString("base.URL")+datasetDataPath;
        URI uri = new UriTemplate(endPointURL).expand( dataType, missionType,year,platform,delivery);
        
         ResponseEntity<String> response = restHeadClient.exchange(uri,HttpMethod.HEAD, null,String.class);
         return   (response.getStatusCode()== HttpStatus.OK);
     }

    
    

    
      /**
     * Convert List to simpler String List.
     * 
     * Convenience method to make list manipulation code cleaner.
     * 
     * @param elementList
     * @return 
     */
    private Collection simplifyList(List<ResultElementType> elementList) {
        ArrayList<String> result = new ArrayList<String>(elementList.size());
        for (ResultElementType element:elementList)
        {
            result.add(element.getResult());
        }
        return result;
    }

    public List listDatasetNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public no.imr.nmd.commons.dataset.jaxb.DatasetType getDatasetDetail(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
