package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface TimeSeriesService {

    public Collection listTimeSeries();

    public Collection listTimeSeriesYears(String timeSeriesName);

    public Collection listCruises(String timeSeriesName, String year);
    
}
