package org.foi.nwtis.azugaj.aplikacija_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;

/**
 * Klasa GlavniPosluzitelj koja je zadužena za otvaranje veze na određenim merežnim vratima/portu. S
 * glavnim klijentom i ostalim poslužiteljima
 * 
 * @author Antonio Žugaj
 *
 */
public class GlavniPosluzitelj {

  protected Konfiguracija konf;
  protected int mreznaVrata = 8000;
  protected int brojRadnika = 5;

  protected volatile int ispis = 0;
  protected volatile int brojDretvi = 0;
  protected volatile int brojZahtjeva = 0;
  protected volatile boolean kraj = false;
  protected volatile boolean serverPokrenut = false;

  public GlavniPosluzitelj(Konfiguracija konf) {
    this.konf = konf;
    this.mreznaVrata = Integer.parseInt(konf.dajPostavku("mreznaVrata"));
    this.brojRadnika = Integer.parseInt(konf.dajPostavku("brojRadnika"));
  }

  /**
   * Metoda koja pokreče sve metode nužne za pokretanje poslužitelja
   */
  public void pokreniPosluzitelja() {
    otvoriMreznaVrata();
  }



  /**
   * Metoda koja otvara mrežna vrata, te kreira i pokreče dretve u slućaju primljenog zahtjeva.
   * 
   * @return Istinu nakon završetka izvođenja
   */
  public boolean otvoriMreznaVrata() {
    if (!provjeriDostupnostVrata())
      return false;

    try (var posluzitelj = new ServerSocket(this.mreznaVrata)) {
      while (!this.kraj) {

        if (this.brojDretvi <= this.brojRadnika) {
          var uticnica = posluzitelj.accept();
          var dretva = new MrezniRadnik(uticnica, konf);
          dretva.setName("azugaj_" + this.brojDretvi);
          dretva.dohvatiPosluzitelja(this);
          this.brojDretvi++;
          dretva.start();
        }
      }
      while (this.brojDretvi > 0) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        posluzitelj.close();
      }
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE,
          "Pogreska kod otvaranja mrežnih vrata: " + e.getMessage());
    }
    return true;
  }

  /**
   * Metoda koja provjerava dostupnost mrežnih vrata.
   * 
   * @return istinitos ako su mrežna vrata dostupna.
   */
  public boolean provjeriDostupnostVrata() {
    ServerSocket test;
    try {
      test = new ServerSocket(this.mreznaVrata);
      test.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "ERROR 05 mrezna vrata su zauzeta");
      return false;
    }
    return true;
  }
}
