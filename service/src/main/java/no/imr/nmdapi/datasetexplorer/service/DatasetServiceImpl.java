package no.imr.nmdapi.datasetexplorer.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.iterators.IteratorIterable;

import no.imr.nmd.commons.cruise.jaxb.CruiseType;
import no.imr.nmd.commons.cruise.jaxb.DatasetType;
import no.imr.nmd.commons.cruise.jaxb.ExistsEnum;
import no.imr.nmdapi.datasetexplorer.dao.CruiseDAO;
import no.imr.nmdapi.datasetexplorer.dao.DatasetDAO;
import no.imr.nmdapi.datasetexplorer.service.beans.CruiseDatasetStatus;
import no.imr.nmdapi.datasetexplorer.service.beans.ImportCount;
import no.imr.nmdapi.datasetexplorer.service.beans.Level;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.SingletonIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dataset service layer implementation.
 *
 * @author a5119
 */
public class DatasetServiceImpl implements DatasetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasetService.class);

    @Autowired
    private DatasetDAO datasetDAO;

    @Autowired
    private CruiseDAO cruiseDAO;

    @Override
    public Collection listExistingDatasets(String missionType, String year, String platform, String delivery) {
        return datasetDAO.listExistingDatasets(missionType, year, platform, delivery);
    }

    @Override
    public Collection listDeliveries(String missionType, String year, String platform) {
        return datasetDAO.listDeliveries(missionType, year, platform);
    }

    @Override
    public Collection listPlatforms(String missionType, String year) {
        return datasetDAO.listPlatforms(missionType, year);
    }

    @Override
    public Collection listYears(String missionType) {
        return datasetDAO.listYears(missionType);
    }

    @Override
    public Collection listMissionTypes() {
        return datasetDAO.listMissionTypes();
    }

    @Override
    public Collection summarizePlatformDataSets(String missionType, String year, String platform) {
        ArrayList<Level> result = new ArrayList<Level>();
        Level level;

        Collection<String> deliveryList = listDeliveries(missionType, year, platform);
        for (String delivery : deliveryList) {
            level = new Level("/" + missionType + "/" + year + "/" + platform + "/", delivery);
            level.setCount(countLoaded(missionType, year, platform, delivery));
            result.add(level);
        }

        return result;
    }

    @Override
    public Collection summarizeYearDataSets(String missionType, String year) {
        ArrayList<Level> result = new ArrayList<Level>();
        Level level;
        ArrayList<Level> deliveryList;
        ImportCount totalImportCount;

        Collection<String> platformList = listPlatforms(missionType, year);
        for (String platform : platformList) {
            totalImportCount = new ImportCount();
            deliveryList = (ArrayList<Level>) summarizePlatformDataSets(missionType, year, platform);
            for (Level deliveryLevel : deliveryList) {
                totalImportCount.add(deliveryLevel.getCount());
            }

            level = new Level("/" + missionType + "/" + year + "/", platform);
            level.setCount(totalImportCount);
            result.add(level);
        }

        return result;

    }

    @Override
    public Collection summarizeMissionTypeDataSets(String missionType) {
        ArrayList<Level> result = new ArrayList<Level>();
        Level level;
        ArrayList<Level> platformList;
        ImportCount totalImportCount;

        Collection<String> yearList = listYears(missionType);
        for (String year : yearList) {
            totalImportCount = new ImportCount();
            platformList = (ArrayList<Level>) summarizeYearDataSets(missionType, year);
            for (Level platformLevel : platformList) {
                totalImportCount.add(platformLevel.getCount());
            }

            level = new Level("/" + missionType + "/", year);
            level.setCount(totalImportCount);
            result.add(level);
        }

        return result;
    }

    @Override
    public Collection summarizeAllDataSets() {
        ArrayList<Level> result = new ArrayList<Level>();
        Level level;
        ArrayList<Level> yearList;
        ImportCount totalImportCount;

        Collection<String> missionTypeList = listMissionTypes();
        for (String missionType : missionTypeList) {
            totalImportCount = new ImportCount();
            yearList = (ArrayList<Level>) summarizeMissionTypeDataSets(missionType);
            for (Level yearLevel : yearList) {
                totalImportCount.add(yearLevel.getCount());
            }

            level = new Level("/", missionType);
            level.setCount(totalImportCount);
            result.add(level);
        }

        return result;
    }

    @Override
    public Object countAllDataSets() {
        ArrayList<Level> missionTypeList;
        ImportCount totalImportCount = new ImportCount();

        missionTypeList = (ArrayList<Level>) summarizeAllDataSets();
        for (Level missionTypeLevel : missionTypeList) {
            totalImportCount.add(missionTypeLevel.getCount());
        }
        return totalImportCount;
    }

    public boolean checkDatasetFileExists(String missionType, String year, String platform, String delivery, String dataType) {
        return datasetDAO.checkDatasetFileExists(missionType, year, platform, delivery, dataType);
    }

    public ImportCount countLoaded(final String missionType, final String year, final String platform, final String delivery) {
        ImportCount result = new ImportCount();

        String cruisePath = "/" + missionType + "/" + year + "/" + platform + "/" + delivery + "/";
        //First look at cruise for identified datasets
        CruiseType cruise = cruiseDAO.getCruiseDetailByCruisePath(cruisePath);
        if (cruise != null) {
            result.incMissionCount();

            for (DatasetType dataset : cruise.getDatasets().getDataset()) {
                if (!dataset.getCollected().equals(ExistsEnum.NO)) {
                    result.incIdentifed();
                }
            }

            //Look at dataset xml         
            ArrayList<String> datasetList = (ArrayList<String>) listExistingDatasets(missionType, year, platform, delivery);
            for (String dataset : datasetList) {
                result.incLoaded();
                if (checkDatasetFileExists(missionType, year, platform, delivery, dataset)) {
                    result.incExists();
                }
            }
            result.decLoaded();

        }
        return result;

    }

    @Override
    public Map listExistingDatasetsDetail(String missionType, String year, String platform, String delivery) {
        return datasetDAO.listExistingDatasetsDetail(missionType, year, platform, delivery);
    }

    @Override
    public Collection listCruiseDatasetTypes() {
        return datasetDAO.listDatasetNames();
    }

    @Override
    public Map summarizeByCruise(String missionType, String year) {

        Collection<String> deliveryList;
        ArrayList<String> dataSets;
        HashMap<String, HashMap> result = new HashMap<String, HashMap>();
        HashMap cruiseDatasets;
        String cruiseNR;

        Collection<String> platformList = listPlatforms(missionType, year);
        for (String platform : platformList) {
            deliveryList = listDeliveries(missionType, year, platform);
            for (String delivery : deliveryList) {
                cruiseDatasets = new HashMap();
                dataSets = (ArrayList<String>) listExistingDatasets(missionType, year, platform, delivery);

                for (String dataType : dataSets) {
                    cruiseDatasets.put(dataType, checkDatasetFileExists(missionType, year, platform, delivery, dataType));
                }
                cruiseNR = (String) cruiseDAO.getCruiseByCruisePath("/" + missionType + "/" + year + "/" + platform + "/" + delivery + "/");
                result.put(cruiseNR, cruiseDatasets);
            }
        }

        return result;
    }

    @Override
    public List summarizeDatasetsStatus(String missionType, String year) {
        List<CruiseDatasetStatus> result = new ArrayList<CruiseDatasetStatus>();

        Collection<String> deliveryList;

        CruiseDatasetStatus cruiseStatus;

        Collection<String> platformList = listPlatforms(missionType, year);
        for (String platform : platformList) {
            deliveryList = listDeliveries(missionType, year, platform);
            for (String delivery : deliveryList) {
                cruiseStatus = getCruiseStatus(missionType, year, platform, delivery);
                if (cruiseStatus != null) {
                    result.add(cruiseStatus);
                }
            }
        }
        Collections.sort(result);
        return result;
    }

    @Override
    public CruiseDatasetStatus getCruiseStatus(String cruisePath) {

        CruiseDatasetStatus result = null;
        String[] parts = cruisePath.split("/");
        if (parts.length == 5) {
            result = getCruiseStatus(parts[1], parts[2], parts[3], parts[4]);
        }
        return result;
    }

    @Override
    public CruiseDatasetStatus getCruiseStatus(String missionType, String year, String platform, String delivery) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        CruiseDatasetStatus result = null;
        GregorianCalendar monthAgo = new GregorianCalendar();
        monthAgo.add(Calendar.MONTH, -1);

        CruiseType cruise = cruiseDAO.getCruiseDetailByCruisePath("/" + missionType + "/" + year + "/" + platform + "/" + delivery + "/");
        if (cruise != null) {
            result = new CruiseDatasetStatus();
            result.setDelivery(delivery);
            result.setPlatform(platform);
            result.setStopDate(dateFormat.format(cruise.getStopTime().toGregorianCalendar().getTime()));

            for (DatasetType dataset : cruise.getDatasets().getDataset()) {
                result.setExistsStatus(dataset.getDataType().name(), dataset.getCollected().name());
                if (checkDatasetFileExists(missionType, year, platform, delivery, dataset.getDataType().name().toLowerCase())) {
                    result.setLoadedStatus(dataset.getDataType().name(), "Y");
                } else {
                    //Hacky hand coding of status values
                    if (cruise.getStopTime().toGregorianCalendar().after(monthAgo)) {
                        //Cruise stopped less than month ago
                        result.setLoadedStatus(dataset.getDataType().name(), "S");
                    } else {
                        result.setLoadedStatus(dataset.getDataType().name(), "L");
                    }

                }
            }
        }
        return result;
    }

    public List listDatasets(String missionType, String year, String platform, String delivery, String datatype,
            String cruiseNumber, String status) {

        ArrayList result = new ArrayList();

        Iterable<String> missionTypes, years, platforms, deliveries, datatypes;

        if (missionType == null) {
            missionTypes = listMissionTypes();
        } else {
            missionTypes = new IteratorIterable(new SingletonIterator(missionType));
        }

        for (String currentMissionType : missionTypes) {
            if (year == null || year.isEmpty()) {
                years = listYears(currentMissionType);
                if (years == null) {
                    years = new IteratorIterable(EmptyIterator.emptyIterator());
                }

            } else {
                years = new IteratorIterable(new SingletonIterator(year));
            }
            for (String currentYear : years) {
                LOGGER.debug("check " + currentMissionType + "/" + currentYear + "/");

                if (platform == null) {
                    platforms = listPlatforms(currentMissionType, currentYear);
                    if (platforms == null) {
                        platforms = new IteratorIterable(EmptyIterator.emptyIterator());
                    }

                } else {
                    platforms = new IteratorIterable(new SingletonIterator(platform));
                }
                for (String currentPlatform : platforms) {
                    if (delivery == null) {
                        deliveries = listDeliveries(currentMissionType, currentYear, currentPlatform);
                        if (deliveries == null) {
                            deliveries = new IteratorIterable(EmptyIterator.emptyIterator());
                        }

                    } else {
                        deliveries = new IteratorIterable(new SingletonIterator(delivery));
                    }
                    for (String currentDelivery : deliveries) {
                        //Now for additional checks
                        LOGGER.debug("check " + currentMissionType + "/" + currentYear + "/" + currentPlatform + "/" + currentDelivery);

                        Map datasets = datasetDAO.listExistingDatasetsDetail(currentMissionType, currentYear, currentPlatform, currentDelivery);

                        for (Object dataset : datasets.keySet()) {

                            HashMap match = new HashMap();
                            match.put("missionType", currentMissionType);
                            match.put("year", currentYear);
                            match.put("platform", currentPlatform);
                            match.put("delivery", currentDelivery);
                            match.put("dataset", dataset);
    //                    match.put("missionType", currentMissionType);
                            //                     match.put("missionType", currentMissionType);
                            result.add(match);
                        }
                    }
                }
            }
        }

        return result;
    }

}
