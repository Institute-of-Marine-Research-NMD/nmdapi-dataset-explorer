package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;
import no.imr.nmdapi.datasetexplorer.dao.TimeSeriesDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class TimeSeriesServiceImpl implements TimeSeriesService {

     @Autowired
    private TimeSeriesDAO timeSeriesDAO;
    
    public Collection listTimeSeries() {
        return timeSeriesDAO.listTimeSeries();
    }

    public Collection listTimeSeriesYears(String timeSeriesName) {
       return timeSeriesDAO.listTimeSeriesYears(timeSeriesName);
    }

    public Collection listCruises(String timeSeriesName, String year) {
     return timeSeriesDAO.listCruises(timeSeriesName, year);
    }

}
