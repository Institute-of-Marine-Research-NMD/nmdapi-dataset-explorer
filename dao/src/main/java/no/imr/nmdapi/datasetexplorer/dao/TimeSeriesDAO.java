package no.imr.nmdapi.datasetexplorer.dao;

import java.util.Collection;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface TimeSeriesDAO {

    public Collection listTimeSeries() ;

    public Collection listTimeSeriesYears(String timeSeriesName);

    public Collection listCruises(String timeSeriesName,String year);
    

}
