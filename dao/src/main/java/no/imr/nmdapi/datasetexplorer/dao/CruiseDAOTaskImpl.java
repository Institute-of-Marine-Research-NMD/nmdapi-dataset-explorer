package no.imr.nmdapi.datasetexplorer.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import no.imr.nmd.commons.cruise.jaxb.CruiseType;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class CruiseDAOTaskImpl implements CruiseDAO {
    private static final Logger LOG = LoggerFactory.getLogger(CruiseDAOTaskImpl.class);
    
     @Autowired
    private Unmarshaller cruiseUnMarshaller;

    @Autowired
    private Configuration config; 
    
    private HashMap<String, String> cruisePathMap;
    HashMap<String,CruiseType> cruiseDetailMap;
   

    public void updateCruise() {
        LOG.debug("Start update Cruises");
                 Runtime rt = Runtime.getRuntime();
         long used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        LOG.debug("Mem"+used);

        HashMap<String, String> newCruisePathMap = new HashMap< String, String>();
        HashMap<String,CruiseType> newCruiseDetailMap   = new HashMap<String, CruiseType>();
        
        String currentPath;
        File filePath;

        ArrayList<String> missionTypes = fileList("");

        for (String missionType : missionTypes) {
            ArrayList<String> years = fileList(missionType);

            for (String year : years) {
                currentPath = expand(File.separator, missionType, year);

                ArrayList<String> platforms = fileList(currentPath);

                for (String platform : platforms) {
                    currentPath = expand(File.separator, missionType, year, platform);

                    ArrayList<String> deliveries = fileList(currentPath);

                     for (String delivery : deliveries) {
                        currentPath = expand(File.separator, missionType, year, platform,delivery,"cruise");
                        
                        filePath = new File(config.getString("base.filePath")+currentPath+"data.xml");
           
                        try {
                            
                            CruiseType cruise = (CruiseType) JAXBIntrospector.getValue(cruiseUnMarshaller.unmarshal(filePath));
                            
                            newCruisePathMap.put(cruise.getCruiseCode(),"/"+expand("/", missionType, year, platform,delivery));
                            newCruiseDetailMap.put("/"+expand("/", missionType, year, platform,delivery),cruise);
                            
                            
                        } catch (JAXBException ex) {
                            java.util.logging.Logger.getLogger(CruiseDAOTaskImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        }
                    }
                }
            }
        
        cruisePathMap = newCruisePathMap;
        cruiseDetailMap = newCruiseDetailMap;
        LOG.debug("End update");
        used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        LOG.debug("Mem"+used);


    }
    
    public Object getCruiseByCruiseNR(String cruiseNR) {
        
        String result="";
        
        if (cruisePathMap.containsKey(cruiseNR)){
            result = cruisePathMap.get(cruiseNR);
        }
        return result;    
    }

   public Object getCruiseByCruisePath(String cruisePath) {
        
        String result="";
       if (cruiseDetailMap.containsKey(cruisePath)){
            result = cruiseDetailMap.get(cruisePath).getCruiseCode();
        }
        
        return result;    
    }
  
      public CruiseType getCruiseDetailByCruisePath(String cruisePath) {
       return cruiseDetailMap.get(cruisePath);
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

  
}
