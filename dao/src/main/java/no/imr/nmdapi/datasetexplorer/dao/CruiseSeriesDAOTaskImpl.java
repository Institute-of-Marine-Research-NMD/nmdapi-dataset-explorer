package no.imr.nmdapi.datasetexplorer.dao;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBIntrospector;
import javax.xml.bind.Unmarshaller;

import no.imr.nmd.commons.cruiseseries.domain.v1.CruiseSerieType;
import no.imr.nmd.commons.cruiseseries.domain.v1.CruiseSeriesType;
import no.imr.nmd.commons.cruiseseries.domain.v1.CruiseType;
import no.imr.nmd.commons.cruiseseries.domain.v1.SampleType;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class CruiseSeriesDAOTaskImpl implements CruiseSeriesDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CruiseSeriesDAOTaskImpl.class);

    @Autowired
    private Unmarshaller cruiseseriesUnMarshaller;

    @Autowired
    private Configuration config;

    private HashMap<String, ArrayList<String>> urlList;

    public void updateCruiseSeries() {
        LOG.debug("Start update Cruise series");
        Runtime rt = Runtime.getRuntime();
        long used = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        LOG.debug("Mem " + used);

        HashMap<String, ArrayList<String>> newUrlList = new HashMap< String, ArrayList<String>>();
        ArrayList<String> yearList;
        String year;
        ArrayList<String> cruiseNRList;

        File filePath = new File(config.getString("cruiseseries.base.filePath"));
        ArrayList<String> cruiseSeriesNameList = new ArrayList<String>();
        String[] directories = filePath.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        for (String name : directories) {
            cruiseSeriesNameList.add(name);

            String dataFilePath = config.getString("cruiseseries.base.filePath") + name + "/data.xml";
            File path = new File(dataFilePath);

            CruiseSeriesType cruiseSeriesSet;
            try {

                LOG.debug("cruise path" + path);
                CruiseSerieType cruiseSeries = (CruiseSerieType) JAXBIntrospector.getValue(cruiseseriesUnMarshaller.unmarshal(path));

                List<SampleType> sampleList = cruiseSeries.getSamples().getSample();
                yearList = new ArrayList<String>();
                for (SampleType sample : sampleList) {
                    year = sample.getSampleTime();
                    yearList.add(year);
                    cruiseNRList = new ArrayList<String>();

                    for (CruiseType cruise : sample.getCruises().getCruise()) {
                        cruiseNRList.add(cruise.getCruisenr());
                    }
                    newUrlList.put(name + "/" + year, cruiseNRList);
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

        urlList = newUrlList;
        LOG.debug("End update");
        used = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        LOG.debug("Mem" + used);

    }

    public Collection listCruiseSeries() {
        return urlList.get("");
    }

    public Collection listCruiseSeriesYears(String cruiseSeriesName) {
        return urlList.get(cruiseSeriesName);
    }

    public Collection listCruises(String cruiseSeriesName, String year) {
        return urlList.get(cruiseSeriesName + "/" + year);
    }

}
