package org.foi.nwtis.azugaj.aplikacija_4.pomocnici;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.Korisnik;

/**
 * 
 * @author Antonio Žugaj
 *
 *         Klasa za provjeru korisnika
 */
public class Autentikacija {

  /**
   * Metoda koja provjerava ispravnost korisnika
   * 
   * @param korisnik - korisnicko ime
   * @param lozinka - lozinka
   * @param ds - baza podataka
   * @return
   */
  public static boolean provjeraKorisnika(String korisnik, String lozinka,
      javax.sql.DataSource ds) {

    Korisnik trazeni = null;

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      String query = "SELECT * FROM KORISNICI WHERE KORIME = ?";
      stmt = con.prepareStatement(query);
      stmt.setString(1, korisnik);

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

    if (trazeni != null) {
      if (trazeni.getLozinka().compareTo(lozinka) == 0) {
        return true;
      }
    }
    return false;
  }

}
