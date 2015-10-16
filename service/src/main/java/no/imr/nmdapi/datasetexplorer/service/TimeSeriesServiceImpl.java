package no.imr.nmdapi.datasetexplorer.service;

import java.util.ArrayList;
import java.util.Collection;
import no.imr.nmdapi.datasetexplorer.dao.CruiseSeriesDAO;
import no.imr.nmdapi.datasetexplorer.dao.SurveyTimeSeriesDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class TimeSeriesServiceImpl implements TimeSeriesService {

    @Autowired
    private SurveyTimeSeriesDAO surveyTimeSeriesDAO;

    @Autowired
    private CruiseSeriesDAO cruiseSeriesDAO;

    
    public Collection listSurveyTimeSeries() {
        return surveyTimeSeriesDAO.listSurveyTimeSeries();
    }

    public Collection listSurveyTimeSeriesSample(String surveyTimeSeriesName) {
       return surveyTimeSeriesDAO.listSurveryTimeSeriesTimes(surveyTimeSeriesName);
    }
    
    public Collection listSurveyTimeSeriesCruiseSeries(String surveyTimeSeriesName){
        return surveyTimeSeriesDAO.listCruisesSeries(surveyTimeSeriesName);
    }

    public Collection listSurveyTimeSeriesCruise(String surveyTimeSeriesName,String period){
        ArrayList result = new ArrayList();
        
        Collection<String> cruiseSeriesList = surveyTimeSeriesDAO.listCruisesSeries(surveyTimeSeriesName);
        for ( String cruiseSeries:cruiseSeriesList) {
           result.addAll(cruiseSeriesDAO.listCruises(cruiseSeries, period));
        }
        
        return result;
    }

}
