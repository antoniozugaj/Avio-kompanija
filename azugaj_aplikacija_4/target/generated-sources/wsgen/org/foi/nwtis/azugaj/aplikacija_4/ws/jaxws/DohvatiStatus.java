
package org.foi.nwtis.azugaj.aplikacija_4.ws.jaxws;

import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.foi.nwtis.podaci.Aerodrom;

@XmlRootElement(name = "dohvatiStatus", namespace = "http://ws.aplikacija_4.azugaj.nwtis.foi.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dohvatiStatus", namespace = "http://ws.aplikacija_4.azugaj.nwtis.foi.org/")
public class DohvatiStatus {

    @XmlElement(name = "arg0", namespace = "")
    private List<Aerodrom> arg0;

    /**
     * 
     * @return
     *     returns List<Aerodrom>
     */
    public List<Aerodrom> getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(List<Aerodrom> arg0) {
        this.arg0 = arg0;
    }

}
