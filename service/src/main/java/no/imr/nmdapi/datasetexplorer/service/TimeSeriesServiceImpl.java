package no.imr.nmdapi.datasetexplorer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import no.imr.nmdapi.datasetexplorer.dao.CruiseSeriesDAO;
import no.imr.nmdapi.datasetexplorer.dao.SurveyTimeSeriesDAO;
import no.imr.nmdapi.datasetexplorer.service.beans.CruiseDatasetStatus;
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
    
    @Autowired
    private CruiseSeriesService cruiseSeriesService;

    
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

    
    public List summarizeDatasetsStatus(String surveyTimeSeriesName, String period) {
        List<CruiseDatasetStatus> result = new ArrayList<CruiseDatasetStatus>();

        Collection<String> cruiseSeriesList = surveyTimeSeriesDAO.listCruisesSeries(surveyTimeSeriesName);
        for ( String cruiseSeries:cruiseSeriesList) {
           result.addAll(cruiseSeriesService.summarizeDatasetsStatus(cruiseSeries, period));
        }
        Collections.sort(result);
        return result;
    }
    
     public List summarizeDatasetsStatus(String surveyTimeSeriesName) {
        List<CruiseDatasetStatus> result = new ArrayList<CruiseDatasetStatus>();

        Collection<String>  periods = surveyTimeSeriesDAO.listSurveryTimeSeriesTimePeriod(surveyTimeSeriesName);
        for (String  period :periods) {
            Collection<String> cruiseSeriesList = surveyTimeSeriesDAO.listCruisesSeries(surveyTimeSeriesName);
        for ( String cruiseSeries:cruiseSeriesList) {
           result.addAll(cruiseSeriesService.summarizeDatasetsStatus(cruiseSeries, period));
        }
     }  
        Collections.sort(result);
        return result;
    }

    
}
