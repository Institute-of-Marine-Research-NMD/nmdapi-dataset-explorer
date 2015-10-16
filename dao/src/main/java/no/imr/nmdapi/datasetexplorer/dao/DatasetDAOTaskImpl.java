package no.imr.nmdapi.datasetexplorer.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import no.imr.nmd.commons.cruise.jaxb.DataTypeEnum;
import no.imr.nmd.commons.dataset.jaxb.DatasetType;
import no.imr.nmd.commons.dataset.jaxb.DatasetsType;
import no.imr.nmdapi.exceptions.NotFoundException;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriTemplate;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class DatasetDAOTaskImpl implements DatasetDAO {

    private static final Logger LOG = LoggerFactory.getLogger(DatasetDAOTaskImpl.class);

    @Autowired
    private Unmarshaller datasetUnMarshaller;

    @Autowired
    private Configuration config;

    //config info
    String deliveryPath = "/nmdapi/dataset/v1/{missionType}/{year}/{platform}/{delivery}";
    String datasetDataPath = "/nmdapi/{dataType}/v1/{missionType}/{year}/{platform}/{delivery}";

    private HashMap<String, ArrayList<String>> urlList;
    private HashSet<String> fileExistsDatasets;
    private ArrayList<String> datasetNames;

    public void updateDataset() {
        LOG.debug("Start update dataset");
          Runtime rt = Runtime.getRuntime();
         long used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        LOG.debug("Mem "+used);
    
        HashMap<String, ArrayList<String>> newUrlList = new HashMap< String, ArrayList<String>>();
        HashSet<String> newFileExistsDatasets = new HashSet<String>();
        String currentPath,datasetFilePath;

        ArrayList<String> missionTypes = fileList("");
       Collections.sort(missionTypes);

        newUrlList.put("", missionTypes);

        for (String missionType : missionTypes) {
            ArrayList<String> years = fileList(missionType);
            
            Collections.sort(years);
            Collections.reverse(years);
            
            newUrlList.put(missionType, years);

            for (String year : years) {
                currentPath = expand(File.separator, missionType, year);
                ArrayList<String> platforms = fileList(currentPath);
               Collections.sort(platforms);
                
                newUrlList.put(currentPath, platforms);

                for (String platform : platforms) {
                    currentPath = expand(File.separator, missionType, year, platform);
                    ArrayList<String> deliveries = fileList(currentPath);
                     Collections.sort(deliveries);

                    newUrlList.put(currentPath, deliveries);

                     for (String delivery : deliveries) {
                        currentPath = expand(File.separator, missionType, year, platform,delivery);
           
                        ArrayList<String> datasets = getLoadedDatasets(missionType, year, platform, delivery);
                        newUrlList.put(currentPath, datasets);
                        
                        for (String dataset:datasets){
                                 currentPath = expand(File.separator, missionType, year, platform, delivery, dataset.toLowerCase());
                                 datasetFilePath = config.getString("base.filePath") + currentPath + "data.xml";
                                 File path = new File(datasetFilePath);
                                 if (path.exists()){
                                     newFileExistsDatasets.add(currentPath);
                                 }
                        }
                    }
                }
            }
        }
        urlList = newUrlList;
        fileExistsDatasets = newFileExistsDatasets;
        
      used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        LOG.debug("Mem "+used);
    
        LOG.debug("End update");

    }

    public Collection listDeliveries(String missionType, String year, String platform) {
        return urlList.get(expand(File.separator, missionType, year, platform));
    }

    public Collection listPlatforms(String missionType, String year) {
        return urlList.get(expand(File.separator, missionType, year));
    }

    public Collection listYears(String missionType) {
        return urlList.get(missionType);
    }

    public Collection listMissionTypes() {
        return urlList.get("");
    }

    public Collection listExistingDatasets(String missionType, String year, String platform, String delivery) {
         return urlList.get(expand(File.separator, missionType,year,platform,delivery));
    }
    
    /*
     * Get datasets that are listed in cruise dataset data.xml
    */
    private ArrayList<String> getLoadedDatasets(String missionType, String year, String platform, String delivery) {
        
      String filePath = config.getString("base.filePath") + expand(File.separator, missionType, year, platform, delivery) + "data.xml";
        File path = new File(filePath);
        ArrayList<String> result = new ArrayList<String>();
        DatasetsType datasets;
 
        try {
            datasets = (DatasetsType) JAXBIntrospector.getValue(datasetUnMarshaller.unmarshal(path));
            result = new ArrayList<String>();
            for (DatasetType datasetDetail : datasets.getDataset()) {
                result.add(datasetDetail.getDataType().name().toLowerCase());
            }
        } catch (JAXBException ex) {
            new NotFoundException(filePath + " not found ."); //TODO handle this better
        }
        return result;
    }

    
    
    
    public Map listExistingDatasetsDetail(String missionType, String year, String platform, String delivery) {

        ArrayList<String> dataSets = (ArrayList<String>) listExistingDatasets(missionType, year, platform, delivery);
        String dataURL;
        HashMap<String, String> result = new HashMap();

        UriTemplate deliveryPathTemplate = new UriTemplate(config.getString("base.URL") + deliveryPath);
        UriTemplate datasetPathTemplate = new UriTemplate(config.getString("base.URL") + datasetDataPath);

        result.put("metadata", deliveryPathTemplate.expand(missionType, year, platform, delivery).toString());

        for (String dataType : dataSets) {
            if (checkDatasetFileExists(missionType, year, platform, delivery, dataType)) {
                dataURL = datasetPathTemplate.expand(dataType, missionType, year, platform, delivery).toString();
            } else {
                dataURL = "N/A";
            }
            result.put(dataType, dataURL);
        }
        return result;
    }
 
    public boolean checkDatasetFileExists(String missionType, String year, String platform, String delivery, String dataType) {
        return  fileExistsDatasets.contains(expand(File.separator, missionType, year, platform, delivery,dataType.toLowerCase()));
    }

    private String expand(String separator, String... args) {
        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            result.append(arg);
            result.append(separator);
        }
        return result.toString();
    }

    private ArrayList<String> fileList(String path) {
        File filePath = new File(config.getString("base.filePath") + path);
        ArrayList<String> result = new ArrayList<String>();
 
        for (String name : filePath.list()) {
            result.add(name);
        }
        return result;
    }

    public List listDatasetNames() {
        if (datasetNames == null ) {
       datasetNames = new ArrayList<String>();
        for (DataTypeEnum dataType:DataTypeEnum.values()) {
          datasetNames.add(dataType.value());
        }
      }
    return datasetNames;
    }
    
}
