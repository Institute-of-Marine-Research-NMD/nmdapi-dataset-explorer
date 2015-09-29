package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface CruiseSeriesService {

    public Collection listCruiseSeries();

    public Collection listCruiseSeriesYears(String cruiseSeriesName);

    public Collection listCruises(String cruiseSeriesName, String year);
    
}
