package no.imr.nmdapi.datasetexplorer.dao;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface CruiseDAO {

    public Object getCruiseByCruiseNR(String cruiseNR);
 
   public Object getCruiseByCruisePath(String cruisePath);
 
}
