package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface TimeSeriesService {

    
     
    public Collection listSurveyTimeSeries() ;
 
    public Collection listSurveyTimeSeriesSample(String surveyTimeSeriesName) ;

    public Collection listSurveyTimeSeriesCruiseSeries(String surveyTimeSeriesName) ;

    public Collection listSurveyTimeSeriesCruise(String surveyTimeSeriesName,String period);
}
