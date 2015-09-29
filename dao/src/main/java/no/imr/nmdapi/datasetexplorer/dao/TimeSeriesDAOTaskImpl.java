package no.imr.nmdapi.datasetexplorer.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import no.imr.commons.nmdcruiseseries.domain.v1.CruiseSerieType;
import no.imr.commons.nmdcruiseseries.domain.v1.CruiseSeriesType;
import no.imr.commons.nmdcruiseseries.domain.v1.CruiseType;
import no.imr.commons.nmdcruiseseries.domain.v1.SampleType;
import no.imr.nmd.commons.dataset.jaxb.DatasetType;
import no.imr.nmd.commons.dataset.jaxb.DatasetsType;
import no.imr.nmdapi.generic.exceptions.NotFoundException;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/** 
 *
 * @author Terry Hannant <a5119>
 */
public class TimeSeriesDAOTaskImpl implements TimeSeriesDAO {
    
    
    private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesDAOTaskImpl.class);

    @Autowired
    private Unmarshaller cruiseseriesUnMarshaller;

    @Autowired
    private Configuration config;

    private HashMap<String, ArrayList<String>> urlList;
    
    public void updateTimeSeries() {
        LOG.debug("Start update time series");
          Runtime rt = Runtime.getRuntime();
         long used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        LOG.debug("Mem "+used);
    
        
        HashMap<String, ArrayList<String>> newUrlList = new HashMap< String, ArrayList<String>>();
        ArrayList<String> yearList;
        String year;
        ArrayList<String> cruiseNRList;
          
        
        
        File filePath = new File(config.getString("timeseries.base.filePath") );
        ArrayList<String> cruiseSeriesNameList  = new ArrayList<String>();
        for (String name : filePath.list()) {
            cruiseSeriesNameList.add(name);
            
            String dataFilePath =config.getString("timeseries.base.filePath") + name+ "/data.xml";
            File path = new File(dataFilePath);

            CruiseSeriesType  cruiseSeriesSet;
            try {
//                cruiseSeriesSet =  (CruiseSeriesType) cruiseseriesUnMarshaller.unmarshal(path);
                  
//    LOG.debug("Length:"+cruiseSeriesSet.getCruiseSerie().size());
//             CruiseSerieType cruiseSeries = cruiseSeriesSet.getCruiseSerie().get(0); //TODO Bad assumption?
             CruiseSerieType cruiseSeries = (CruiseSerieType) JAXBIntrospector.getValue(cruiseseriesUnMarshaller.unmarshal(path));
    
             List<SampleType> sampleList = cruiseSeries.getSample();
               yearList = new ArrayList<String>();
                for(SampleType sample:sampleList){
                   year =  sample.getSampleTime();
                   yearList.add(year);
                   cruiseNRList = new ArrayList<String>();
                   for (CruiseType cruise :sample.getCruises().getCruise()){
                       cruiseNRList.add(cruise.getCruisenr());
                }
                   newUrlList.put(name+"/"+year, cruiseNRList);
                }
                Collections.sort(yearList);
                Collections.reverse(yearList);
                newUrlList.put(name, yearList);
                
            } catch (JAXBException ex) {
              ex.printStackTrace();
                //TODO handle this better
            }
            
            newUrlList.put("", cruiseSeriesNameList);
        }
         used  = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        LOG.debug("Mem "+used);
    
        urlList = newUrlList;
        LOG.debug("End update");

    }

    public Collection listTimeSeries() {
        return urlList.get("");
    }

    public Collection listTimeSeriesYears(String timeSeriesName) {
        return urlList.get(timeSeriesName);
    }

    public Collection listCruises(String timeSeriesName, String year) {
        return urlList.get(timeSeriesName+"/"+year);
    }

    
    

}
