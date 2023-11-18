package org.foi.nwtis.azugaj.aplikacija_3.dretve;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.LetAviona;

public class DretvaPreuzimanja extends Thread {

  private boolean kraj = false;

  public Konfiguracija konfig;
  public DataSource ds;


  public DretvaPreuzimanja(Konfiguracija konfiguracija, DataSource dSource) {
    konfig = konfiguracija;
    ds = dSource;
  }

  @Override
  public void run() {

    var korime = konfig.dajPostavku("OpenSkyNetwork.korisnik");
    var lozinka = konfig.dajPostavku("OpenSkyNetwork.lozinka");
    var datumOd = konfig.dajPostavku("preuzimanje.od");
    var datumDo = konfig.dajPostavku("preuzimanje.do");
    var ciklus = Integer.parseInt(konfig.dajPostavku("ciklus.trajanje"));


    Date vrijemeOd = null;;
    Date vrijemeDo = null;;
    var pom = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    try {
      vrijemeOd = pom.parse(datumOd + " 00:00:00");
      vrijemeDo = pom.parse(datumDo + " 23:59:59");
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Date zadnji = dohvatiZadnjiZapis();

    Date pocetak = zadnji;

    if (zadnji == null || (pocetak.getTime() < vrijemeOd.getTime())) {
      pocetak = vrijemeOd;
    }


    while (!kraj) {

      int brojac = 0;
      Calendar kal = Calendar.getInstance();
      kal.setTime(pocetak);
      kal.add(Calendar.DATE, 1);
      pocetak = kal.getTime();
      if (pocetak.getTime() > vrijemeDo.getTime()) {
        kraj = true;
        break;
      }

      Calendar kalendar = Calendar.getInstance();
      kalendar.setTime(pocetak);
      kalendar.set(Calendar.HOUR_OF_DAY, 0);
      kalendar.set(Calendar.MINUTE, 0);
      kalendar.set(Calendar.SECOND, 0);
      Date vOd = kalendar.getTime();

      kalendar.setTime(pocetak);
      kalendar.set(Calendar.HOUR_OF_DAY, 23);
      kalendar.set(Calendar.MINUTE, 59);
      kalendar.set(Calendar.SECOND, 59);
      Date vDo = kalendar.getTime();


      brojac = preuzmiPodatke(korime, lozinka, vOd, vDo);
      try {
        Thread.sleep(ciklus * 1000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        break;
      }
    }


    super.run();
  }

  private Date dohvatiZadnjiZapis() {
    String query = "SELECT * FROM LETOVI_POLASCI ORDER BY id DESC LIMIT 1";
    String datum = null;
    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        datum = rs.getString("STORED");
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
    if (datum == null)
      return null;
    else {
      // TODO
      var pom = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
      try {
        Date vrijeme = pom.parse(datum);
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return null;
    }
  }

  private int preuzmiPodatke(String korime, String lozinka, Date vrijemeOd, Date vrijemeDo) {
    OSKlijent osKlijent = new OSKlijent(korime, lozinka);

    var trazeni = dohvatiIcaoZaSkidanje();
    int brojac = 0;
    for (String icao : trazeni) {
      List<LetAviona> let = new ArrayList<>();
      try {
        let = osKlijent.getDepartures(icao, vrijemeOd.getTime() / 1000, vrijemeDo.getTime() / 1000);
      } catch (NwtisRestIznimka e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        break;
      }
      spremiLetove(let, vrijemeOd);
      brojac += let.size();
    }

    return brojac;
  }

  private List<String> dohvatiIcaoZaSkidanje() {

    String query = "SELECT * FROM AERODROMI_LETOVI WHERE STATUS = 'Da'";
    List<String> trazeni = new ArrayList<>();

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {

        trazeni.add(rs.getString("ICAO"));

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

  private void spremiLetove(List<LetAviona> let, Date vrijeme) {

    for (LetAviona la : let) {

      String query =
          "INSERT INTO LETOVI_POLASCI (ICAO24, FIRSTSEEN, ESTDEPARTUREAIRPORT, LASTSEEN, ESTARRIVALAIRPORT, CALLSIGN, ESTDEPARTUREAIRPORTHORIZDISTANCE, ESTDEPARTUREAIRPORTVERTDISTANCE, ESTARRIVALAIRPORTHORIZDISTANCE, ESTARRIVALAIRPORTVERTDISTANCE, DEPARTUREAIRPORTCANDIDATESCOUNT, ARRIVALAIRPORTCANDIDATESCOUNT, STORED) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      if (la.getEstArrivalAirport() == null) {
        la.setEstArrivalAirport("-");
      }
      PreparedStatement stmt = null;
      var unosFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      try (var con = ds.getConnection()) {
        stmt = con.prepareStatement(query);
        stmt.setString(1, la.getIcao24());
        stmt.setInt(2, la.getFirstSeen());
        stmt.setString(3, la.getEstDepartureAirport());
        stmt.setInt(4, la.getLastSeen());
        stmt.setString(5, la.getEstArrivalAirport());
        stmt.setString(6, la.getCallsign());
        stmt.setInt(7, la.getEstDepartureAirportHorizDistance());
        stmt.setInt(8, la.getEstDepartureAirportVertDistance());
        stmt.setInt(9, la.getEstArrivalAirportHorizDistance());
        stmt.setInt(10, la.getEstArrivalAirportVertDistance());
        stmt.setInt(11, la.getDepartureAirportCandidatesCount());
        stmt.setInt(12, la.getArrivalAirportCandidatesCount());
        stmt.setString(13, unosFormat.format(vrijeme));

        stmt.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
        kraj = true;
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

  }

}
