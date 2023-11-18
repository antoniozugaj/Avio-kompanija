package org.foi.nwtis.azugaj.aplikacija_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/**
 * Klasa koja pokrece glavni poslužitelj i predaje mu konfiguracijsku datoteku.
 * 
 * @author Antonio Žugaj
 *
 */
public class PokretacPosluzitelja {
  /**
   * Početna metoda.
   * 
   * @param args Argumenti iz konzole. Predstavljaju ime datoteke u kojoj se nalaze konfiguracije
   *        poslužitelja.
   */
  public static void main(String[] args) {
    var pokretac = new PokretacPosluzitelja();
    if (!pokretac.provjeriArgumente(args)) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Nije upisan naziv datoteke ili je datoteka ne podržanog formata!");
      return;
    }

    try {
      var konf = pokretac.ucitajPostavke(args[0]);
      var glavniPosluzitelj = new GlavniPosluzitelj(konf);
      glavniPosluzitelj.pokreniPosluzitelja();
    } catch (NeispravnaKonfiguracija e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Pogreška kod učitavanja postavki iz datoteke!" + e.getMessage());
    }
  }

  /**
   * Metoda koja provjerava ispravnost upisanog argumenta.
   * 
   * @param args - argument koji predstavlja ime datoteke.
   * @return istinitost ako je datoteka ispravno napisana.
   */
  private boolean provjeriArgumente(String[] args) {
    if (args.length == 1) {
      String reg = "^[a-zA-Z0-9._ \\\\-]+.(txt|bin|json|xml|yaml|yml)$";
      Pattern pattern = Pattern.compile(reg);
      Matcher mat = pattern.matcher(args[0]);
      return mat.matches();
    }
    return false;
  }

  /**
   * Metoda koja ucitava postavke iz datoteke.
   * 
   * @param nazivDatoteke u kojoj se nalaze podatci konfiguracije.
   * @return konfiguraciju.
   * @throws NeispravnaKonfiguracija ako konfiguracij nije ispravna.
   */
  Konfiguracija ucitajPostavke(String nazivDatoteke) throws NeispravnaKonfiguracija {
    return KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
  }

}
