package org.foi.nwtis.azugaj.aplikacija_4.ws;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
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
 *         Klasa za endpoint meteo/
 */
@WebService(serviceName = "meteo")
public class WsMeteo {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  @Inject
  ServletContext context;

  /**
   * Metoda koja vraca meteo za icao
   * 
   * @param icao - icao aerodroma
   * @return meteo podaci
   */
  @WebMethod
  public MeteoPodaci dajMeteo(@WebParam String icao) {



    // Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    // String OWMkey = konfig.dajPostavku("OpenWeatherMap.apikey");

    String OWMkey = "c88d8b34b88dcf2de9309e83041155b4";


    String query = "SELECT * FROM AIRPORTS WHERE ICAO = ?";

    PreparedStatement stmt = null;

    MeteoPodaci response = null;

    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);


      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {

        String koordinate = rs.getString("COORDINATES");
        var razdvojeno = koordinate.split(",");

        OWMKlijent owm = new OWMKlijent(OWMkey);
        response = owm.getRealTimeWeather(razdvojeno[0].strip(), razdvojeno[1].strip());
      }
      rs.close();

    } catch (SQLException | NwtisRestIznimka e) {
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
    return response;
  }

}
