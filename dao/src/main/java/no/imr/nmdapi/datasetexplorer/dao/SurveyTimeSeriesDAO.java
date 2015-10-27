package no.imr.nmdapi.datasetexplorer.dao;

import java.util.Collection;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface SurveyTimeSeriesDAO {

    public Collection listSurveyTimeSeries() ;

    public Collection listSurveryTimeSeriesTimes(String surveyTimeSeriesName);

    public Collection listSurveryTimeSeriesTimePeriod(String surveyTimeSeriesName);
        
    public Collection<String> listCruisesSeries(String surveyTimeSeriesName) ;
    
    

}
