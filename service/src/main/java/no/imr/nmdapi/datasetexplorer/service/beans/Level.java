package no.imr.nmdapi.datasetexplorer.service.beans;

/**
 *
 * @author a5119
 */
public class Level {

    private final String parentPath;
    private final String name;
    private ImportCount count;

    public Level(String parentPath, String name) {
        this.parentPath = parentPath;
        this.name = name;
    }

    public ImportCount getCount() {
        return count;
    }

    public void setCount(ImportCount count) {
        this.count = count;
    }

    public String getParentPath() {
        return parentPath;
    }

    public String getName() {
        return name;
    }
    
    
    
    
}
