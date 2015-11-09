package no.imr.nmdapi.datasetexplorer.dao.pojo;

import java.util.Objects;

/**
 *
 * @author Terry Hannant <a5119>
 */
public class ShipIdentifer {

    String cruiseCode;
    String shipName;

    public String getCruiseCode() {
        return cruiseCode;
    }

    public void setCruiseCode(String cruiseCode) {
        this.cruiseCode = cruiseCode;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.cruiseCode);
        hash = 37 * hash + Objects.hashCode(this.shipName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ShipIdentifer other = (ShipIdentifer) obj;
        if (!Objects.equals(this.cruiseCode, other.cruiseCode)) {
            return false;
        }
        if (!Objects.equals(this.shipName, other.shipName)) {
            return false;
        }
        return true;
    }
    
      
    
}
