package org.foi.nwtis.azugaj.aplikacija_4.ws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.azugaj.aplikacija_4.pomocnici.Autentikacija;
import org.foi.nwtis.podaci.Korisnik;
import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

/**
 * 
 * @author Antonio Žugaj
 * 
 *         Klasa za web servis korisnici
 *
 */
@WebService(serviceName = "korisnici")
public class WsKorisnici {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;


  /**
   * Metoda koaj daje sve korisnike
   * 
   * @param korisnik - korisnicko ime
   * @param lozinka - lozinka
   * @param traziImeKorisnika - ime za filtriranje
   * @param traziPrezimeKorisnika - prezime za filtriranje
   * @return
   */
  @WebMethod
  public List<Korisnik> dajKorisnike(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String traziImeKorisnika, @WebParam String traziPrezimeKorisnika) {

    if (!Autentikacija.provjeraKorisnika(korisnik, lozinka, ds))
      return null;

    List<Korisnik> korisnici = new ArrayList<>();



    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      String query = "";
      if (traziImeKorisnika != null && traziPrezimeKorisnika != null) {
        query = "SELECT * FROM KORISNICI WHERE IME LIKE ? AND PREZIME LIKE ?";
        stmt = con.prepareStatement(query);
        stmt.setString(1, "%" + traziImeKorisnika + "%");
        stmt.setString(2, "%" + traziPrezimeKorisnika + "%");
      } else if (traziImeKorisnika != null) {
        query = "SELECT * FROM KORISNICI WHERE IME LIKE ?";
        stmt = con.prepareStatement(query);
        stmt.setString(1, "%" + traziImeKorisnika + "%");
      } else if (traziPrezimeKorisnika != null) {
        query = "SELECT * FROM KORISNICI WHERE PREZIME LIKE ?";
        stmt = con.prepareStatement(query);
        stmt.setString(1, "%" + traziPrezimeKorisnika + "%");
      } else {
        query = "SELECT * FROM KORISNICI";
        stmt = con.prepareStatement(query);
      }

      ResultSet rs = stmt.executeQuery();


      while (rs.next()) {

        Korisnik novi = new Korisnik();
        novi.setIme(rs.getString("IME"));
        novi.setPrezime(rs.getString("PREZIME"));
        novi.setEmail(rs.getString("EMAIL"));
        novi.setKorIme(rs.getString("KORIME"));
        novi.setLozinka(rs.getString("LOZINKA"));
        korisnici.add(novi);
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

    return korisnici;
  }

  /**
   * Metoda za dohvacaje korisnika
   * 
   * @param korisnik - korisnicko ime
   * @param lozinka - lozinka
   * @param traziKorisnika - korisnicko ime
   * @return
   */
  @WebMethod
  public Korisnik dajKorisnik(@WebParam String korisnik, @WebParam String lozinka,
      @WebParam String traziKorisnika) {

    if (!Autentikacija.provjeraKorisnika(korisnik, lozinka, ds))
      return null;

    Korisnik trazeni = null;

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      String query = "SELECT * FROM KORISNICI WHERE KORIME = ?";
      stmt = con.prepareStatement(query);
      stmt.setString(1, traziKorisnika);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {

        Korisnik novi = new Korisnik();
        novi.setIme(rs.getString("IME"));
        novi.setPrezime(rs.getString("PREZIME"));
        novi.setEmail(rs.getString("EMAIL"));
        novi.setKorIme(rs.getString("KORIME"));
        novi.setLozinka(rs.getString("LOZINKA"));
        trazeni = novi;
        break;
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

    return trazeni;
  }


  /**
   * Metoda za dodavanje korisnika
   * 
   * @param korisnik
   * @return
   */
  @WebMethod
  public boolean dodajKorisnika(@WebParam Korisnik korisnik) {

    String query =
        "INSERT INTO KORISNICI (KORIME, LOZINKA, IME, PREZIME, EMAIL) VALUES (?, ?, ?, ?, ?)";
    int promjena = 0;
    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, korisnik.getKorIme());
      stmt.setString(2, korisnik.getLozinka());
      stmt.setString(3, korisnik.getIme());
      stmt.setString(4, korisnik.getPrezime());
      stmt.setString(5, korisnik.getEmail());

      promjena = stmt.executeUpdate();
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
    if (promjena > 0) {
      return true;
    } else {
      return false;
    }
  }
}
