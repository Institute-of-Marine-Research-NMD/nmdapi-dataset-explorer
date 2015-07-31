/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.imr.nmdapi.datasetexplorer.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import no.imr.nmdapi.generic.exceptions.NotFoundException;
import no.imr.nmdapi.generic.nmdmission.domain.v1.DatatypeElementType;
import no.imr.nmdapi.generic.nmdmission.domain.v1.ExistsEnum;
import no.imr.nmdapi.generic.nmdmission.domain.v1.MissionType;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriTemplate;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class DatasetDAOFileImpl implements DatasetDAO{

    private static final Logger LOG = LoggerFactory.getLogger(DatasetDAOFileImpl.class);
    
    @Autowired
    private Unmarshaller missionUnMarshaller;

    @Autowired 
    private Configuration config;
    
     //config info
    String deliveryPath ="/nmdapi/nmdmission/v1/{missionType}/{year}/{platform}/{delivery}";
    String datasetDataPath = "/nmdapi/nmd{dataType}/v1/{missionType}/{year}/{platform}/{delivery}";

    
    public Collection listDeliveries(String missionType, String year, String platform) {
         String filePath = config.getString("base.filePath")+expand( File.separator, missionType,year,platform);
       File path = new File(filePath);
       return fileList(path);
    }

    public Collection listPlatforms(String missionType, String year) {
       String filePath = config.getString("base.filePath")+expand( File.separator, missionType,year);
       File path = new File(filePath);
       return fileList(path);
    }

    public Collection listYears(String missionType) {
       String filePath = config.getString("base.filePath")+expand( File.separator, missionType);
       File path = new File(filePath);
       return fileList(path);
    }

    public Collection listMissionTypes() {
       String filePath = config.getString("base.filePath");
       File path = new File(filePath);
       return fileList(path);
    }

    public Collection listExistingDatasets(String missionType, String year, String platform, String delivery) {
       String filePath = config.getString("base.filePath")+expand( File.separator, missionType,year,platform,delivery)+"/mission/data.xml";
       File path = new File(filePath);
 
      ArrayList<String> result = new ArrayList<String>();
      MissionType mission;
        try {
            mission = (MissionType) missionUnMarshaller.unmarshal(path);
            result = new ArrayList<String>();
             for (DatatypeElementType missionDetail:mission.getDatatypes().getDatatype())
             {
                if (missionDetail.getExists() == ExistsEnum.YES){
                    result.add(missionDetail.getName());
                }
             }
        } catch (JAXBException ex) {
             new NotFoundException( filePath +  " not found ."); //TODO handle this better
      }
     return result;        
    }

    public Map listExistingDatasetsDetail(String missionType, String year, String platform, String delivery) {
             
    ArrayList<String> dataSets =  (ArrayList<String>) listExistingDatasets(missionType,year,platform,delivery);
    String dataURL;
    HashMap<String,String> result = new HashMap();
    
     UriTemplate deliveryPathTemplate = new UriTemplate(config.getString("base.URL")+deliveryPath);
     UriTemplate datasetPathTemplate = new UriTemplate(config.getString("base.URL")+datasetDataPath);
  
     result.put("metadata",deliveryPathTemplate.expand(missionType,year,platform,delivery).toString());

                for (String dataType:dataSets)
                {
                              if (checkDataSetLoaded(missionType,year,platform,delivery,dataType))    {
                                            dataURL =datasetPathTemplate.expand(dataType, missionType,year,platform,delivery).toString();
                                       }
                              else {
                               dataURL ="N/A";
                              } 
                    result.put(dataType,dataURL);
                }
        return result;
    }

    public boolean checkDataSetLoaded(String missionType, String year, String platform, String delivery, String dataType) {
        String filePath = config.getString("base.filePath")+expand( File.separator, missionType,year,platform,delivery,dataType)+"data.xml";
       File path = new File(filePath);
       return   path.exists();
    }
    
    
    private String expand(String separator, String... args) {
        StringBuilder result = new StringBuilder();
        for (String arg : args) {
              result.append(arg);
              result.append(separator);
        }
        return result.toString();
    }

    private Collection fileList(File path) {
        ArrayList<String> result = new ArrayList<String>();
        for (String name:path.list())
        {
            result.add(name);
        }
        return result;
    }

}
