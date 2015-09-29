package no.imr.nmdapi.datasetexplorer.service;

import java.util.Collection;
import no.imr.nmdapi.datasetexplorer.dao.CruiseDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class CruiseServiceImpl implements CruiseService {

     @Autowired
    private CruiseDAO cruiseDAO;

    public Object getCruisePath(String cruiseNR) {
        return cruiseDAO.getCruiseByCruiseNR(cruiseNR);
    }
    

}
