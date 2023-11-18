
package org.foi.nwtis.azugaj.aplikacija_4.ws.jaxws;

import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.foi.nwtis.podaci.AerodromStatus;

@XmlRootElement(name = "dohvatiStatusResponse", namespace = "http://ws.aplikacija_4.azugaj.nwtis.foi.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dohvatiStatusResponse", namespace = "http://ws.aplikacija_4.azugaj.nwtis.foi.org/")
public class DohvatiStatusResponse {

    @XmlElement(name = "return", namespace = "")
    private List<AerodromStatus> _return;

    /**
     * 
     * @return
     *     returns List<AerodromStatus>
     */
    public List<AerodromStatus> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(List<AerodromStatus> _return) {
        this._return = _return;
    }

}
