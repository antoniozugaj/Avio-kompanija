package org.foi.nwtis.azugaj.aplikacija_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.Konfiguracija;

/**
 * Klasa koja predstavlja dretvu koja izvršava provjere i komunikacije s klijentom i drugim
 * poslužiteljima
 * 
 * @author Antonio Žugaj
 *
 */
public class MrezniRadnik extends Thread {

  protected Socket mreznaUticnica;
  protected Konfiguracija konfig;
  protected GlavniPosluzitelj posluzitelj;

  /**
   * Konstruktor klase MrezniRadnik
   * 
   * @param mreznaUticnica - mrežna utičnica prosljeđena od glavnog poslužitelja.
   * @param konfig konfiguracijski podaci
   */
  public MrezniRadnik(Socket mreznaUticnica, Konfiguracija konfig) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.konfig = konfig;
  }

  /**
   * Metoda pokretanja dretve.
   */
  @Override
  public synchronized void start() {
    super.start();
  }

  /**
   * Metoda koja dohvaca klasu posluzitelja koji je pozvao dretvu.
   * 
   * @param pozivac - posluzitelj.
   */
  public void dohvatiPosluzitelja(GlavniPosluzitelj pozivac) {
    this.posluzitelj = pozivac;
  }

  /**
   * Metoda u kojoj se cita naredba koja je pristigla na glavni posluzitelj. poziva se njezino
   * izvršavanje i vraca odgovor posiljatelju zahtjeva.
   */
  @Override
  public void run() {
    try {
      var citac = new BufferedReader(
          new InputStreamReader(this.mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(this.mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      var poruka = new StringBuilder();
      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        if (posluzitelj.ispis == 1)
          System.out.println(red);
        poruka.append(red);
      }
      this.mreznaUticnica.shutdownInput();
      String odgovor = this.obradiZahtjev(poruka.toString());
      pisac.write(odgovor);
      pisac.flush();
      this.mreznaUticnica.shutdownOutput();
      this.mreznaUticnica.close();
      posluzitelj.brojDretvi--;
      mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getLogger(PokretacPosluzitelja.class.getName()).log(Level.SEVERE,
          "Pogreska u komunikaciji preko mreznih vrata: " + e.getMessage());
    }
  }

  /**
   * Metoda u kojoj se određuje o kakvom je zahtjevu rječ. pozivaju se procedure ovisno o zahtjevu.
   * 
   * @param zahtjev - zahtjev koji je pristigao na posluzitelja.
   * @return odgovor.
   */
  public String obradiZahtjev(String zahtjev) {

    String odgovor = "";
    var odvojeno = zahtjev.split(" ");

    switch (odvojeno[0]) {
      case "STATUS": {
        odgovor = status(zahtjev);
        break;
      }
      case "INIT": {
        odgovor = init(zahtjev);
        break;
      }
      case "KRAJ": {
        odgovor = kraj(zahtjev);
        break;
      }
      case "PAUZA": {
        odgovor = pauza(zahtjev);
        break;
      }
      case "INFO": {
        odgovor = info(odvojeno);
        break;
      }
      case "UDALJENOST": {
        odgovor = udaljenost(zahtjev, odvojeno);
        break;
      }

      default: {
        odgovor = "ERROR 05 Zahtijev nije prepoznat";
      }
    }

    return odgovor;
  }

  /**
   * Metoda koja izracunava udaljenost
   * 
   * @param zahtjev - cijeli zahtjev
   * @param odvojeno - zahtjev kao polje
   * @return
   */
  private String udaljenost(String zahtjev, String[] odvojeno) {
    if (!posluzitelj.serverPokrenut) {
      return "ERROR 01 Posluzitelj nije aktivan";
    } else {
      Pattern uzorakIzracun = Pattern.compile(
          "^UDALJENOST ([0-9,-]{1,4}.[0-9,-]+) ([0-9,-]{1,4}.[0-9,-]+) ([0-9,-]{1,4}.[0-9]+) ([0-9,-]{1,4}.[0-9]+)$");
      if (uzorakIzracun.matcher(zahtjev).matches()) {
        double sirina1 = Double.parseDouble(odvojeno[1]);
        double duzina1 = Double.parseDouble(odvojeno[2]);
        double sirina2 = Double.parseDouble(odvojeno[3]);
        double duzina2 = Double.parseDouble(odvojeno[4]);
        posluzitelj.brojZahtjeva++;
        return "OK " + izracunajUdaljenost(duzina1, sirina1, duzina2, sirina2);
      } else {
        return "ERROR 05 Neispravan zahtijev";
      }
    }
  }

  /**
   * Metoda koja izračunava udaljenost dvije točke na sveri. Algoritam preuzet s:
   * https://community.esri.com/t5/coordinate-reference-systems-blog/distance-on-a-sphere-the-haversine-formula/ba-p/902128
   * 
   * @param duzina1 Geografska duzina prve tocke.
   * @param sirina1 Geografska sirina prve tocke.
   * @param duzina2 Geografska duzina prve druge.
   * @param sirina2 Geografska sirina prve druge.
   * @return udaljenost u kilometrima.
   */
  private double izracunajUdaljenost(double duzina1, double sirina1, double duzina2,
      double sirina2) {

    double zemljaR = 6371;

    double lat1 = Math.toRadians(sirina1);
    double lat2 = Math.toRadians(sirina2);

    double razlikaLat = Math.toRadians(sirina2 - sirina1);
    double razlikaLon = Math.toRadians(duzina2 - duzina1);

    double a = Math.pow(Math.sin(razlikaLat / 2), 2)
        + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(razlikaLon / 2), 2);

    double c = (2 * Math.asin(Math.sqrt(a))) * zemljaR;
    c = Math.round(c * 100);
    return c / 100;
  }

  private String info(String[] odvojeno) {
    if (!posluzitelj.serverPokrenut) {
      return "ERROR 01 Posluzitelj nije aktivan";
    } else {
      if (odvojeno.length < 2) {
        return "ERROR 05 Naredba nije prepoznata";
      }
      if (odvojeno[1].contains("DA")) {
        if (posluzitelj.ispis == 1) {
          return "ERROR 03 Informacije o zahtjevima se vec ispisuju";
        } else {
          posluzitelj.ispis = 1;
        }
      } else if (odvojeno[1].contains("NE")) {
        if (posluzitelj.ispis == 0) {
          return "ERROR 04 Informacije o zahtjevima se vec ne ispisuju";
        } else {
          posluzitelj.ispis = 0;
        }
      } else {
        return "ERROR 05 Naredba nije prepoznata";
      }
    }
    return "OK";
  }

  /**
   * Metoda za pauziranje servera
   * 
   * @param zahtjev
   * @return poruku o pauziranju
   */
  private String pauza(String zahtjev) {
    if (!posluzitelj.serverPokrenut) {
      return "ERROR 01 Posluzitelj nije aktivan";
    } else {
      Pattern uzorak = Pattern.compile("^PAUZA$");
      if (!provjeriIspravnostKomande(zahtjev, uzorak)) {
        return "ERROR 05 Naredba nije ispravna";
      }
      posluzitelj.serverPokrenut = false;
      return "OK " + posluzitelj.brojZahtjeva;
    }
  }

  /**
   * Matoda za aktiviranje servera
   * 
   * @param zahtjev
   * @return- poruka o aktiviranju
   */
  private String init(String zahtjev) {
    if (posluzitelj.serverPokrenut) {
      return "ERROR 02 Posluzitelj je vec inicijaliziran";
    } else {
      Pattern uzorak = Pattern.compile("^INIT$");
      if (!provjeriIspravnostKomande(zahtjev, uzorak)) {
        return "ERROR 05 Naredba nije ispravna";
      }
      posluzitelj.serverPokrenut = true;
      posluzitelj.brojZahtjeva = 0;
      return "OK";
    }
  }

  /**
   * Matoda koja vraca status
   * 
   * @param zahtjev
   * @return poruku o statusu
   */
  private String status(String zahtjev) {
    Pattern uzorak = Pattern.compile("^STATUS$");
    if (!provjeriIspravnostKomande(zahtjev, uzorak)) {
      return "ERROR 05 Naredba nije ispravna";
    }
    if (posluzitelj.serverPokrenut) {
      return "OK 1";
    } else {
      return "OK 0";
    }
  }

  /**
   * Metoda koja izvršava naredbu KRAJ.
   * 
   * @param zahtjev - zahtjev od korisnika.
   * @param odvojeno - zahtjev prelomljen na djelove.
   * @return odgovor na zahtjev.
   */
  public String kraj(String zahtjev) {

    Pattern uzorakKraj = Pattern.compile("^KRAJ$");
    if (!provjeriIspravnostKomande(zahtjev, uzorakKraj)) {
      return "ERROR 05 Naredba nije ispravna";
    }
    posluzitelj.kraj = true;
    return "OK";
  }



  /**
   * metoda za provjeru ispravnosti komande u odnosu na uzorak.
   * 
   * @param komanda - naredba koja je pristigla.
   * @param uzorak - uzorak koji se mora ispuniti.
   * @return istinu ako je uzorak ispunjen.
   */
  public boolean provjeriIspravnostKomande(String komanda, Pattern uzorak) {
    Matcher matcher = uzorak.matcher(komanda);
    return matcher.matches();
  }



  /**
   * Metoda prekida.
   */
  @Override
  public void interrupt() {
    super.interrupt();
  }

}
