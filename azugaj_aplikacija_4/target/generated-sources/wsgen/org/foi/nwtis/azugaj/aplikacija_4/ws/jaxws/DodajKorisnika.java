
package org.foi.nwtis.azugaj.aplikacija_4.ws.jaxws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.foi.nwtis.podaci.Korisnik;

@XmlRootElement(name = "dodajKorisnika", namespace = "http://ws.aplikacija_4.azugaj.nwtis.foi.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dodajKorisnika", namespace = "http://ws.aplikacija_4.azugaj.nwtis.foi.org/")
public class DodajKorisnika {

    @XmlElement(name = "arg0", namespace = "")
    private Korisnik arg0;

    /**
     * 
     * @return
     *     returns Korisnik
     */
    public Korisnik getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(Korisnik arg0) {
        this.arg0 = arg0;
    }

}
