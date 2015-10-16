package no.imr.nmdapi.datasetexplorer.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import no.imr.nmd.commons.cruise.jaxb.CruiseType;
import no.imr.nmd.commons.cruise.jaxb.DatasetType;
import no.imr.nmd.commons.cruise.jaxb.ExistsEnum;
import no.imr.nmdapi.datasetexplorer.dao.CruiseDAO;
import no.imr.nmdapi.datasetexplorer.dao.DatasetDAO;
import no.imr.nmdapi.datasetexplorer.service.beans.ImportCount;
import no.imr.nmdapi.datasetexplorer.service.beans.Level;

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

        //First look at cruise for identified datasets
        CruiseType cruise = cruiseDAO.getCruiseDetailByCruisePath("/" + missionType + "/" + year + "/" + platform + "/" + delivery + "/");
        if (cruise != null) {
            result.incMissionCount();
    //    result.incIdentifed();

            //cruiseNR =cruise.getCruiseCode();
            for (DatasetType dataset : cruise.getDatasets().getDataset()) {
                if (dataset.getCollected().equals(ExistsEnum.YES)) {
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
    public Map summarizeDatasetsStatus(String missionType, String year) {
        HashMap<String, HashMap> result = new HashMap<String, HashMap>();

        Collection<String> deliveryList;

        HashMap cruiseDatasetStatus;
        String cruiseNR;
        GregorianCalendar monthAgo = new GregorianCalendar();
        monthAgo.add(Calendar.MONTH, -1);

        Collection<String> platformList = listPlatforms(missionType, year);
        for (String platform : platformList) {
            deliveryList = listDeliveries(missionType, year, platform);
            for (String delivery : deliveryList) {

                cruiseDatasetStatus = new HashMap();

                HashMap<String, String> datasetExists = new HashMap<String, String>();
                HashMap<String, String> datasetLoaded = new HashMap<String, String>();

                //Warning abstraction leakage occuring
                //TODO need to refactor to clean up leakage
                CruiseType cruise = cruiseDAO.getCruiseDetailByCruisePath("/" + missionType + "/" + year + "/" + platform + "/" + delivery + "/");
                if (cruise != null) {

                    cruiseNR = delivery + " " + platform;

                    for (DatasetType dataset : cruise.getDatasets().getDataset()) {

                        datasetExists.put(dataset.getDataType().name(), dataset.getCollected().name());
                        if (checkDatasetFileExists(missionType, year, platform, delivery, dataset.getDataType().name().toLowerCase())) {
                            datasetLoaded.put(dataset.getDataType().name(), "Y");
                        } else {
                            //Hacky hand coding of status values
                            if (cruise.getStopTime().toGregorianCalendar().after(monthAgo)) {
                                //Cruise stopped less than month ago
                                datasetLoaded.put(dataset.getDataType().name(), "S");
                            } else {
                                datasetLoaded.put(dataset.getDataType().name(), "L");
                            }

                        }
                    }

                    cruiseDatasetStatus.put("exists", datasetExists);
                    cruiseDatasetStatus.put("loaded", datasetLoaded);
                    
                    result.put(cruiseNR, cruiseDatasetStatus);
                }
            }
        }
        return result;
    }

}
