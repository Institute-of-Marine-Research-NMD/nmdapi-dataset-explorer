package no.imr.nmdapi.datasetexplorer.service.beans;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class CruiseDatasetStatus implements Comparable<CruiseDatasetStatus>{
    
    String delivery;
    String platform;
    String stopDate;
    Map loaded = new HashMap<String,String>();
    Map exists = new HashMap<String,String>();

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Map getLoaded() {
        return loaded;
    }

    public void setLoadedStatus(String name,String status) {
        this.loaded.put(name, status);
    }

    public Map getExists() {
        return exists;
    }

    public void setExistsStatus(String name,String status) {
        this.exists.put(name,status);
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    
    
    @Override
    public int compareTo(CruiseDatasetStatus s) {
        return this.delivery.compareTo(s.delivery);
    }
    
  
    
    

}
