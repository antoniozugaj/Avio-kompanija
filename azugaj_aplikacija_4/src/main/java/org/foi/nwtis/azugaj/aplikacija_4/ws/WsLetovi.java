package org.foi.nwtis.azugaj.aplikacija_4.ws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.azugaj.aplikacija_4.pomocnici.Autentikacija;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.servlet.ServletContext;

/**
 * 
 * @author Antonio Žugaj
 * 
 *         Web servis za letovi
 *
 */
@WebService(serviceName = "letovi")
public class WsLetovi {

  @Inject
  ServletContext context;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;


  /**
   * Metoda koja vraca letove u vremenskom intervalu
   * 
   * @param korisnik
   * @param lozinka
   * @param icao
   * @param danOd
   * @param danDo
   * @param odBroja
   * @param broj
   * @return
   */
  @WebMethod
  public List<LetAviona> dajPolaskeInterval(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao, @WebParam String danOd, @WebParam String danDo, @WebParam int odBroja,
      @WebParam int broj) {

    if (!Autentikacija.provjeraKorisnika(korisnik, lozinka, ds))
      return null;

    List<LetAviona> letovi = new ArrayList<>();
    Date vrijemeOd = null;
    Date vrijemeDo = null;
    var pom = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    try {
      vrijemeOd = pom.parse(danOd + " 00:00:00");
      vrijemeDo = pom.parse(danDo + " 23:59:59");
    } catch (ParseException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    var unosFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    PreparedStatement stmt = null;
    int brojac = 0;
    try (var con = ds.getConnection()) {
      String query =
          "SELECT * FROM LETOVI_POLASCI WHERE ESTDEPARTUREAIRPORT = ? AND STORED >= ? AND STORED <= ?";
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);
      stmt.setString(2, unosFormat.format(vrijemeOd));
      stmt.setString(3, unosFormat.format(vrijemeDo));

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {

        brojac++;
        if (brojac < odBroja)
          continue;

        if (brojac >= (odBroja + broj))
          break;

        LetAviona let = new LetAviona();
        let.setIcao24(rs.getString("ICAO24"));
        let.setFirstSeen(Integer.parseInt(rs.getString("FIRSTSEEN")));
        let.setEstDepartureAirport(rs.getString("ESTDEPARTUREAIRPORT"));
        let.setLastSeen(Integer.parseInt(rs.getString("LASTSEEN")));
        let.setEstArrivalAirport(rs.getString("ESTARRIVALAIRPORT"));
        let.setCallsign(rs.getString("CALLSIGN"));
        let.setEstDepartureAirportHorizDistance(
            Integer.parseInt(rs.getString("ESTDEPARTUREAIRPORTHORIZDISTANCE")));
        let.setEstDepartureAirportVertDistance(
            Integer.parseInt(rs.getString("ESTDEPARTUREAIRPORTVERTDISTANCE")));
        let.setEstArrivalAirportHorizDistance(
            Integer.parseInt(rs.getString("ESTARRIVALAIRPORTHORIZDISTANCE")));
        let.setEstArrivalAirportVertDistance(
            Integer.parseInt(rs.getString("ESTARRIVALAIRPORTVERTDISTANCE")));
        let.setDepartureAirportCandidatesCount(
            Integer.parseInt(rs.getString("DEPARTUREAIRPORTCANDIDATESCOUNT")));
        let.setArrivalAirportCandidatesCount(
            Integer.parseInt(rs.getString("ARRIVALAIRPORTCANDIDATESCOUNT")));
        letovi.add(let);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

    }
    return letovi;
  }

  /**
   * Metoda koja vraca letove na odredeni dan
   * 
   * @param korisnik
   * @param lozinka
   * @param icao
   * @param dan
   * @param odBroja
   * @param broj
   * @return
   */
  @WebMethod
  public List<LetAviona> dajPolaskeNaDan(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao, @WebParam String dan, @WebParam int odBroja, @WebParam int broj) {

    if (!Autentikacija.provjeraKorisnika(korisnik, lozinka, ds))
      return null;

    List<LetAviona> letovi = new ArrayList<>();
    Date vrijemeOd = null;
    Date vrijemeDo = null;
    var pom = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    try {
      vrijemeOd = pom.parse(dan + " 00:00:00");
      vrijemeDo = pom.parse(dan + " 23:59:59");
    } catch (ParseException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    var unosFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    PreparedStatement stmt = null;
    int brojac = 0;
    try (var con = ds.getConnection()) {
      String query =
          "SELECT * FROM LETOVI_POLASCI WHERE ESTDEPARTUREAIRPORT = ? AND STORED >= ? AND STORED <= ?";
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);
      stmt.setString(2, unosFormat.format(vrijemeOd));
      stmt.setString(3, unosFormat.format(vrijemeDo));

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {

        brojac++;
        if (brojac < odBroja)
          continue;

        if (brojac >= (odBroja + broj))
          break;

        LetAviona let = new LetAviona();
        let.setIcao24(rs.getString("ICAO24"));
        let.setFirstSeen(Integer.parseInt(rs.getString("FIRSTSEEN")));
        let.setEstDepartureAirport(rs.getString("ESTDEPARTUREAIRPORT"));
        let.setLastSeen(Integer.parseInt(rs.getString("LASTSEEN")));
        let.setEstArrivalAirport(rs.getString("ESTARRIVALAIRPORT"));
        let.setCallsign(rs.getString("CALLSIGN"));
        let.setEstDepartureAirportHorizDistance(
            Integer.parseInt(rs.getString("ESTDEPARTUREAIRPORTHORIZDISTANCE")));
        let.setEstDepartureAirportVertDistance(
            Integer.parseInt(rs.getString("ESTDEPARTUREAIRPORTVERTDISTANCE")));
        let.setEstArrivalAirportHorizDistance(
            Integer.parseInt(rs.getString("ESTARRIVALAIRPORTHORIZDISTANCE")));
        let.setEstArrivalAirportVertDistance(
            Integer.parseInt(rs.getString("ESTARRIVALAIRPORTVERTDISTANCE")));
        let.setDepartureAirportCandidatesCount(
            Integer.parseInt(rs.getString("DEPARTUREAIRPORTCANDIDATESCOUNT")));
        let.setArrivalAirportCandidatesCount(
            Integer.parseInt(rs.getString("ARRIVALAIRPORTCANDIDATESCOUNT")));
        letovi.add(let);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      Logger.getGlobal().log(Level.SEVERE, e.getMessage());
    } finally {
      try {
        if (stmt != null && !stmt.isClosed())
          stmt.close();
      } catch (SQLException e) {
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      }

    }
    return letovi;
  }

  /**
   * Metoda koja vraca letove na odredeni dan preko OSklijenta
   * 
   * @param korisnik
   * @param lozinka
   * @param icao
   * @param dan
   * @return
   */
  @WebMethod
  public List<LetAviona> dajPolaskeNaDanOS(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao, @WebParam String dan) {

    if (!Autentikacija.provjeraKorisnika(korisnik, lozinka, ds))
      return null;

    List<LetAviona> letovi = new ArrayList<>();
    Date vrijemeOd = null;
    Date vrijemeDo = null;
    var pom = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    try {
      vrijemeOd = pom.parse(dan + " 00:00:00");
      vrijemeDo = pom.parse(dan + " 23:59:59");
    } catch (ParseException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    // var konfig = (Konfiguracija) context.getAttribute("konfig");
    // var korime = konfig.dajPostavku("OpenSkyNetwork.korisnik");
    // var loz = konfig.dajPostavku("OpenSkyNetwork.lozinka");

    var korime = "azugaj";
    var loz = "Zugy007a";


    OSKlijent osKlijent = new OSKlijent(korime, lozinka);

    try {
      letovi =
          osKlijent.getDepartures(icao, vrijemeOd.getTime() / 1000, vrijemeDo.getTime() / 1000);
    } catch (NwtisRestIznimka e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return letovi;
  }

}
