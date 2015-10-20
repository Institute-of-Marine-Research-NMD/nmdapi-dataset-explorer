package no.imr.nmdapi.datasetexplorer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import no.imr.nmdapi.datasetexplorer.dao.CruiseDAO;
import no.imr.nmdapi.datasetexplorer.dao.CruiseSeriesDAO;
import no.imr.nmdapi.datasetexplorer.service.beans.CruiseDatasetStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class CruiseSeriesServiceImpl implements CruiseSeriesService {

    @Autowired
    private CruiseSeriesDAO cruiseSeriesDAO;
    
    @Autowired
    private DatasetService datasetService;
    
    @Autowired
    private CruiseDAO cruiseDAO;
    
     
    
    public Collection listCruiseSeries() {
        return cruiseSeriesDAO.listCruiseSeries();
    }

    public Collection listCruiseSeriesYears(String cruiseSeriesName) {
       return cruiseSeriesDAO.listCruiseSeriesYears(cruiseSeriesName);
    }

    public Collection listCruises(String cruiseSeriesName, String year) {
     return cruiseSeriesDAO.listCruises(cruiseSeriesName, year);
    }
    
    
   public List summarizeDatasetsStatus(String cruiseSeriesName, String year) {
        List<CruiseDatasetStatus> result = new ArrayList<CruiseDatasetStatus>();

        CruiseDatasetStatus cruiseStatus;
        String cruisePath;
        Collection<String> cruiseList = cruiseSeriesDAO.listCruises(cruiseSeriesName, year);
        
       for (String cruise:cruiseList) {
           cruiseStatus = new CruiseDatasetStatus();
           cruisePath =  (String) cruiseDAO.getCruiseByCruiseNR(cruise);
           cruiseStatus = datasetService.getCruiseStatus(cruisePath);
           if (cruiseStatus != null) {
               result.add(cruiseStatus);
            }
       }
  
        Collections.sort(result);
        return result;
    }
    
    

}
