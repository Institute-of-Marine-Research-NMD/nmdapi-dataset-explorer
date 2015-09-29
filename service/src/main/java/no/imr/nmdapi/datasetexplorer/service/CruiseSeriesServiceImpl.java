package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;
import no.imr.nmdapi.datasetexplorer.dao.CruiseSeriesDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class CruiseSeriesServiceImpl implements CruiseSeriesService {

     @Autowired
    private CruiseSeriesDAO cruiseSeriesDAO;
    
    public Collection listCruiseSeries() {
        return cruiseSeriesDAO.listCruiseSeries();
    }

    public Collection listCruiseSeriesYears(String cruiseSeriesName) {
       return cruiseSeriesDAO.listCruiseSeriesYears(cruiseSeriesName);
    }

    public Collection listCruises(String cruiseSeriesName, String year) {
     return cruiseSeriesDAO.listCruises(cruiseSeriesName, year);
    }

}
