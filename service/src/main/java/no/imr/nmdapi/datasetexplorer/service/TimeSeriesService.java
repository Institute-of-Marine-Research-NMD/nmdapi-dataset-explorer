package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface TimeSeriesService {

    
     
    public Collection listSurveyTimeSeries() ;
 
    public Collection listSurveyTimeSeriesSample(String surveyTimeSeriesName) ;

    public Collection listSurveyTimeSeriesCruiseSeries(String surveyTimeSeriesName) ;

    public Collection listSurveyTimeSeriesCruise(String surveyTimeSeriesName,String period);
    
    public List summarizeDatasetsStatus(String surveyTimeSeriesName, String period);
  
     public List summarizeDatasetsStatus(String surveyTimeSeriesName);
     
     public String getStoxPath(String stoxID);
  
}
