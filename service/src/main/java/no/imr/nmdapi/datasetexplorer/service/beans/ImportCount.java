package no.imr.nmdapi.datasetexplorer.service.beans;

/**
 *
 * @author a5119
 */
public class ImportCount {
    private  long loaded;
    private  long exists;
    private  long identified;
    private long missionCount;

    public long getMissionCount() {
        return missionCount;
    }

    public void setMissionCount(long missionCount) {
        this.missionCount = missionCount;
    }
 
    
     public ImportCount() {
        this.loaded = 0;
        this.exists = 0;
        this.identified=0;
    }

    public ImportCount(long identified,long loaded,long existing,long missionCount ) {
        this.identified = identified;
        this.loaded = loaded;
        this.exists = exists;
        this.missionCount = missionCount;
    }
    
    public void incIdentifed()
    {
        this.identified++;
    }

     public void incLoaded()
    {
        this.loaded++;
    }

     public void incExists()
    {
        this.exists++;
    }

     
     public void incMissionCount()
    {
        this.missionCount++;
    }

     
    public void add(ImportCount count) {
        this.identified = this.identified + count.getIdentified();
        this.loaded = this.loaded + count.getLoaded();
        this.exists = this.exists + count.getExists();
        this.missionCount = this.missionCount + count.missionCount;
    }

    public long getLoaded() {
        return loaded;
    }

    public long getExists() {
        return exists;
    }

    public long getIdentified() {
        return identified;
    }

    public void decIdentified() {
        this.identified--;
    }

    public void decLoaded() {
        this.loaded--;
    }

    
    
    
    
}
    
    
