package org.foi.nwtis.azugaj.aplikacija_4.ws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.azugaj.aplikacija_4.pomocnici.Autentikacija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.AerodromStatus;
import org.foi.nwtis.podaci.Lokacija;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

/**
 * 
 * @author Antonio Žugaj
 * 
 *         Klasa za web servis aerodromi
 *
 */
@WebService(serviceName = "aerodromi")
public class WsAerodromi {


  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;


  /**
   * Metoda koja vraca sve aerodrome kojima se spremaju letovi
   * 
   * @param korisnik - korisnicko ime
   * @param lozinka - lozinka
   * @return
   */
  @WebMethod
  public List<Aerodrom> dajAerodromeZaLetove(@WebParam String korisnik, @WebParam String lozinka) {

    if (!Autentikacija.provjeraKorisnika(korisnik, lozinka, ds))
      return null;

    List<Aerodrom> aerodromi = new ArrayList<>();
    List<String> listaIcao = new ArrayList<>();

    String query = "SELECT * FROM AERODROMI_LETOVI";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);

      ResultSet rs = stmt.executeQuery();


      while (rs.next()) {
        listaIcao.add(rs.getString("ICAO"));
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

    for (String icao : listaIcao) {
      query = "SELECT * FROM AIRPORTS WHERE ICAO = ?";

      stmt = null;
      try (var con = ds.getConnection()) {
        stmt = con.prepareStatement(query);
        stmt.setString(1, icao);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {

          String koordinate = rs.getString("COORDINATES");
          var razdvojeno = koordinate.split(",");
          var lokacija = new Lokacija();
          lokacija.setLatitude(razdvojeno[0].strip());
          lokacija.setLongitude(razdvojeno[1].strip());
          Aerodrom ad = new Aerodrom(rs.getString("ICAO"), rs.getString("NAME"),
              rs.getString("ISO_COUNTRY"), lokacija);
          aerodromi.add(ad);
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
    }
    return aerodromi;
  }


  /**
   * Metoda za dodavanje aerodroma na listu skidanja
   * 
   * @param korisnik - korisnicko ime
   * @param lozinka - lozinka
   * @param icao - aerodrom
   * @return
   */
  @WebMethod
  public boolean dodajAerodromZaLetove(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao) {

    if (!Autentikacija.provjeraKorisnika(korisnik, lozinka, ds))
      return false;

    String query = "INSERT INTO AERODROMI_LETOVI (ICAO, STATUS) VALUES (?,?)";
    int brojRedaka = 0;
    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);
      stmt.setString(2, "Da");


      brojRedaka = stmt.executeUpdate();
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

    // TODO Websocket
    if (brojRedaka > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metoda za pauziranje skidajna letova za aerodrom
   * 
   * @param korisnik - korisnicko ime
   * @param lozinka - lozinka
   * @param icao - aerodrom
   * @return
   */
  @WebMethod
  public boolean pauzirajAerodromZaLetov(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao) {

    if (!Autentikacija.provjeraKorisnika(korisnik, lozinka, ds))
      return false;

    String query = "UPDATE AERODROMI_LETOVI SET STATUS = 'Pauza' WHERE ICAO = ?";
    int brojRedaka = 0;
    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);
      brojRedaka = stmt.executeUpdate();
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
    if (brojRedaka > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metoda za aktiviranje statusa skidanja za letove
   * 
   * @param korisnik - korisnicko ime
   * @param lozinka - lozinka
   * @param icao - aerodrom
   * @return
   */
  @WebMethod
  public boolean aktivirajAerodromZaLetov(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String icao) {

    if (!Autentikacija.provjeraKorisnika(korisnik, lozinka, ds))
      return false;

    String query = "UPDATE AERODROMI_LETOVI SET STATUS = 'Da' WHERE ICAO = ?";
    int brojRedaka = 0;
    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);
      brojRedaka = stmt.executeUpdate();
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
    if (brojRedaka > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metoda koaj dohvaca status aerodromima
   * 
   * @param aerodromi
   * @return Podaci o aerodromima s statusom
   */
  @WebMethod
  public List<AerodromStatus> dohvatiStatus(@WebParam List<Aerodrom> aerodromi) {

    List<AerodromStatus> odgovor = new ArrayList<>();
    for (Aerodrom aerodrom : aerodromi) {

      String query = "SELECT * FROM AERODROMI_LETOVI WHERE ICAO = ?";
      PreparedStatement stmt = null;
      try (var con = ds.getConnection()) {
        stmt = con.prepareStatement(query);
        stmt.setString(1, aerodrom.getIcao());

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

          AerodromStatus novi = new AerodromStatus(aerodrom, rs.getString("STATUS"));
          odgovor.add(novi);
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

    }
    return odgovor;
  }


}
