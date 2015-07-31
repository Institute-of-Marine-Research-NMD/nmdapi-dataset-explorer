package no.imr.nmdapi.datasetexplorer.service.beans;

/**
 *
 * @author a5119
 */
public class ImportCount {
    private  long total;
    private  long actual;
    private long missionCount;

    public long getMissionCount() {
        return missionCount;
    }

    public void setMissionCount(long missionCount) {
        this.missionCount = missionCount;
    }
 
    
     public ImportCount() {
        this.total = 0;
        this.actual = 0;
    }

    public ImportCount(long total,long actual,long missionCount ) {
        this.total = total;
        this.actual = actual;
        this.missionCount = missionCount;
    }

    public long getTotal() {
        return total;
    }

    public long getActual() {
        return actual;
    }
    
    public void incTotal()
    {
        this.total++;
    }

     public void incActual()
    {
        this.actual++;
    }

     public void incMissionCount()
    {
        this.missionCount++;
    }

     
    public void add(ImportCount count) {
        this.total = this.total + count.total;
        this.actual = this.actual + count.actual;
        this.missionCount = this.missionCount + count.missionCount;
    }

    
}
    
    
