package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface CruiseSeriesService {

    public Collection listCruiseSeries();

    public Collection listCruiseSeriesYears(String cruiseSeriesName);

    public Collection listCruises(String cruiseSeriesName, String year);
   
    List summarizeDatasetsStatus(String cruiseSeriesName);
     
    List summarizeDatasetsStatus(String cruiseSeriesName, String year);
    
}
