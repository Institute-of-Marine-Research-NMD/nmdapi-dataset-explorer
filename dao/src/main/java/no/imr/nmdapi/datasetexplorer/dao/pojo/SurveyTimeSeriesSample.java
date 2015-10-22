package no.imr.nmdapi.datasetexplorer.dao.pojo;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class SurveyTimeSeriesSample {

    String stoxID;
    String stoxURL;
    String zipURL;
    String sampleTime;

    public String getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }

    public String getStoxID() {
        return stoxID;
    }

    public void setStoxID(String stoxID) {
        this.stoxID = stoxID;
    }

    public String getStoxURL() {
        return stoxURL;
    }

    public void setStoxURL(String stoxURL) {
        this.stoxURL = stoxURL;
    }

    public String getZipURL() {
        return zipURL;
    }

    public void setZipURL(String zipURL) {
        this.zipURL = zipURL;
    }
    
     
    
}
