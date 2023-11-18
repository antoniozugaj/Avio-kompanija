package org.foi.nwtis.azugaj.konfiguracije;

import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Klasa konfiguracija za rad s postavkama konfiguracije u json formatu
 * 
 * @author Matija Novak
 */
public class KonfiguracijaJson extends KonfiguracijaApstraktna {

  /**
   * konstanta
   */
  public static final String TIP = "json";

  /**
   * Konstruktor
   * 
   * @param nazivDatoteke - naziv datotek
   */
  public KonfiguracijaJson(String nazivDatoteke) {
    super(nazivDatoteke);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
    // TODO Auto-generated method stub

  }

  @Override
  public void ucitajKonfiguraciju() throws NeispravnaKonfiguracija {
    // TODO Auto-generated method stub

  }

}
