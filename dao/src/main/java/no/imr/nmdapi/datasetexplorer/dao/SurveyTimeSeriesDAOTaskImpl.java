package no.imr.nmdapi.datasetexplorer.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

import no.imr.nmd.commons.surveytimeseries.jaxb.CruiseSeriesDescriptionType;
import no.imr.nmd.commons.surveytimeseries.jaxb.SampleType;
import no.imr.nmd.commons.surveytimeseries.jaxb.SurveyTimeSeriesType;
import no.imr.nmdapi.datasetexplorer.dao.pojo.SurveyTimeSeriesSample;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/** 
 *
 * @author Terry Hannant <a5119>
 */
public class SurveyTimeSeriesDAOTaskImpl implements SurveyTimeSeriesDAO {
    
    
    private static final Logger LOG = LoggerFactory.getLogger(SurveyTimeSeriesDAOTaskImpl.class);

    @Autowired
    private Unmarshaller surveytimeseriesUnMarshaller;

    @Autowired
    private Configuration config;

    private HashMap<String, ArrayList<String>> urlList;
     HashMap<String, ArrayList<SurveyTimeSeriesSample>> sampleTimeMap = new HashMap< String, ArrayList<SurveyTimeSeriesSample>>();
 
    
    public void updateTimeSeries() {
        LOG.debug("Start update time series");
          Runtime rt = Runtime.getRuntime();
         long used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
          LOG.debug("Memory usage before update:"+used);

                        
        
        HashMap<String, ArrayList<String>> newUrlList = new HashMap< String, ArrayList<String>>();
        HashMap<String, ArrayList<SurveyTimeSeriesSample>> newSampleList = new HashMap< String, ArrayList<SurveyTimeSeriesSample>>();
        
        File filePath = new File(config.getString("timeseries.base.filePath") );
        for (String name : filePath.list()) {
            
            String dataFilePath =config.getString("timeseries.base.filePath") + name+ "/data.xml";
            File path = new File(dataFilePath);
            try {
            SurveyTimeSeriesType surveyTimeSeries = (SurveyTimeSeriesType) JAXBIntrospector.getValue(surveytimeseriesUnMarshaller.unmarshal(path));
            
               ArrayList<String> cruiseSeriesNameList  = new ArrayList<String>();
               ArrayList<SurveyTimeSeriesSample> sampleList  = new ArrayList<SurveyTimeSeriesSample>();
    
                List<SampleType> surveySampleList = surveyTimeSeries.getSamples().getSampleType();
                for (SampleType surveySample:surveySampleList) {
                    SurveyTimeSeriesSample sample = new SurveyTimeSeriesSample();
                    sample.setStoxID(surveySample.getStoxProjectId());
                    sample.setSampleTime(surveySample.getSampleTime());
                    sampleList.add(sample);
                }

                List<CruiseSeriesDescriptionType> surveyCruiseSeriesList = surveyTimeSeries.getCruiseSeries().getCruiseSeriesS();
                for ( CruiseSeriesDescriptionType surveyCruiseSeries:surveyCruiseSeriesList) {
                   cruiseSeriesNameList.add(surveyCruiseSeries.getValue());
                }
                newUrlList.put(name, cruiseSeriesNameList);
                newSampleList.put(name, sampleList);
            } catch (JAXBException ex) {
              ex.printStackTrace();
                //TODO handle this better
            } 
            
        }
         used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
         LOG.debug("Memory usage after update:"+used);
    
        urlList = newUrlList;
        sampleTimeMap = newSampleList;

        LOG.debug("End update");
    }

    public Collection listSurveyTimeSeries() {
        return urlList.keySet();
    }

    public Collection listSurveryTimeSeriesTimes(String surveyTimeSeriesName) {
        return sampleTimeMap.get(surveyTimeSeriesName);
    }

    public Collection listCruisesSeries(String surveyTimeSeriesName) {
        return urlList.get(surveyTimeSeriesName);
    }

    
    

}
