
package org.foi.nwtis.azugaj.aplikacija_4.ws.jaxws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

@XmlRootElement(name = "dajMeteoResponse", namespace = "http://ws.aplikacija_4.azugaj.nwtis.foi.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dajMeteoResponse", namespace = "http://ws.aplikacija_4.azugaj.nwtis.foi.org/")
public class DajMeteoResponse {

    @XmlElement(name = "return", namespace = "")
    private MeteoPodaci _return;

    /**
     * 
     * @return
     *     returns MeteoPodaci
     */
    public MeteoPodaci getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(MeteoPodaci _return) {
        this._return = _return;
    }

}
