package no.imr.nmdapi.datasetexplorer.dao;

import no.imr.nmd.commons.cruise.jaxb.CruiseType;

/**
 *
 * @author Terry Hannant <a5119>
 */
public interface CruiseDAO {

    public Object getCruiseByCruiseNR(String cruiseNR);
 
   public Object getCruiseByCruisePath(String cruisePath);

   public CruiseType getCruiseDetailByCruisePath(String cruisePath);
   
   
}
