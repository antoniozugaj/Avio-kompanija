package org.foi.nwtis.azugaj.aplikacija_2.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.StatusUdaljenosti;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostDoAerodroma;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * 
 * @author Antonio Žugaj
 * 
 *         Klasa koja sadrzi endpoint-ove za /aerodrom zahtjeve
 *
 */

@Path("aerodromi")
@RequestScoped
public class RestAerodromi {

  @Inject
  ServletContext context;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;


  /**
   * Endpoint za / putanju
   * 
   * @param odBr - indeks prvog zapisa
   * @param br - broj zapisa po stranici
   * @param traziNaziv - naziv za filtriranje
   * @param traziDrzavu - drzava za filtriranje
   * @return lista Aerodroma
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response aerodromiOdBroja(@DefaultValue("1") @QueryParam("odBroja") String odBr,
      @DefaultValue("20") @QueryParam("broj") String br,
      @QueryParam("traziNaziv") String traziNaziv, @QueryParam("traziDrzavu") String traziDrzavu) {


    int broj;
    broj = Integer.parseInt(br);
    int odBroja;
    odBroja = Integer.parseInt(odBr);



    List<Aerodrom> aerodromi = new ArrayList<>();


    PreparedStatement stmt = null;
    int brojac = 0;
    try (var con = ds.getConnection()) {
      String query = "";
      if (traziNaziv != null && traziDrzavu != null) {
        query = "SELECT * FROM AIRPORTS WHERE NAME LIKE ? AND ISO_COUNTRY = ?";
        stmt = con.prepareStatement(query);
        stmt.setString(1, "%" + traziNaziv + "%");
        stmt.setString(2, traziDrzavu);
      } else if (traziNaziv != null) {
        query = "SELECT * FROM AIRPORTS WHERE NAME LIKE ?";
        stmt = con.prepareStatement(query);
        stmt.setString(1, "%" + traziNaziv + "%");
      } else if (traziDrzavu != null) {
        query = "SELECT * FROM AIRPORTS WHERE ISO_COUNTRY = ?";
        stmt = con.prepareStatement(query);
        stmt.setString(1, traziDrzavu);
      } else {
        query = "SELECT * FROM AIRPORTS";
        stmt = con.prepareStatement(query);
      }

      ResultSet rs = stmt.executeQuery();


      while (rs.next()) {

        brojac++;
        if (brojac < odBroja)
          continue;

        if (brojac >= (odBroja + broj))
          break;

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
    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(aerodromi);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }


  /**
   * Klasa koja obraduje get zahtjev /{icao}. Namjenjena je za vracanje podataka o aerodromu.
   * 
   * @param icao Identifikator aerodroma.
   * @return podaci o aerodromu.
   */
  @GET
  @Path("/{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom(@PathParam("icao") String icao) {

    Aerodrom aerodrom = dohvatiAerodrom(icao);

    if (aerodrom == null) {
      return Response.status(404).build();
    } else {
      var gson = new Gson();
      var jsonAerodrmi = gson.toJson(aerodrom);
      var odgovor = Response.ok().entity(jsonAerodrmi).build();

      return odgovor;
    }
  }

  /**
   * Klasa koja vraca Udaljenosti izmedu dva aerodroma. Obraduje get zahtjev {icaoOd}/{icaoDo}.
   * 
   * @param icaoFrom id aerodroma polaska.
   * @param icaoTo id aerodroma dolaska.
   * @return Listu udaljenosti.
   */
  @Path("{icaoOd}/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiIzmeduAerodoma(@PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo) {

    var udaljenosti = new ArrayList<Udaljenost>();

    String query = "SELECT ICAO_FROM, ICAO_TO, COUNTRY, DIST_CTRY FROM AIRPORTS_DISTANCE_MATRIX "
        + "WHERE ICAO_FROM = ? AND ICAO_TO = ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icaoFrom);
      stmt.setString(2, icaoTo);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String drzava = rs.getString("COUNTRY");
        float udaljenost = rs.getFloat("DIST_CTRY");
        var u = new Udaljenost(drzava, udaljenost);
        udaljenosti.add(u);
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
    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }


  /**
   * Klasa koja obraduje get zahtjev {icao}/udaljenosti.
   * 
   * @param icao id aerodroma.
   * @param odBr Broj od kojeg se pocinju ispisivati aerodromi. Default vrijednost je 1.
   * @param br Broj koliko se zapisa prikazuje po stranici. Default je 20.
   * @return Listu udaljenosti.
   */
  @GET
  @Path("{icao}/udaljenosti")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiAerodoma(@PathParam("icao") String icao,
      @DefaultValue("1") @QueryParam("odBroja") String odBr,
      @DefaultValue("20") @QueryParam("broj") String br) {

    int broj;
    broj = Integer.parseInt(br);
    int odBroja;
    odBroja = Integer.parseInt(odBr);

    var udaljenosti = new ArrayList<UdaljenostDoAerodroma>();

    String query = "SELECT * FROM AIRPORTS_DISTANCE_MATRIX WHERE ICAO_FROM = ?";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icao);
      ResultSet rs = stmt.executeQuery();


      int brojac = 0;
      while (rs.next()) {

        brojac++;
        if (brojac < odBroja)
          continue;

        if (brojac >= (odBroja + broj))
          break;

        String drzava = rs.getString("COUNTRY");
        String aerodromDo = rs.getString("ICAO_TO");
        float udaljenost = rs.getFloat("DIST_TOT");
        var u = new Udaljenost(drzava, udaljenost);
        var ua = new UdaljenostDoAerodroma(aerodromDo, u);
        udaljenosti.add(ua);

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
    var gson = new Gson();
    var jsonAerodrmi = gson.toJson(udaljenosti);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }


  /**
   * Endpoint za icao/izracun/icao
   * 
   * @param icaoOd - izvorisni aerodrom
   * @param icaoDo - odredisni aerodorm
   * @return - udaljenost
   */
  @GET
  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajIzracun(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {

    String query = "SELECT * FROM AIRPORTS WHERE ICAO = ? OR ICAO = ?";
    List<Aerodrom> aerodromi = new ArrayList<>();

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, icaoOd);
      stmt.setString(2, icaoDo);

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

    String poruka = posaljiZahtjev("UDALJENOST " + aerodromi.get(0).getLokacija().getLatitude()
        + " " + aerodromi.get(0).getLokacija().getLongitude() + " "
        + aerodromi.get(0).getLokacija().getLatitude() + " "
        + aerodromi.get(1).getLokacija().getLongitude());

    var gson = new Gson();
    var odvojeno = poruka.split(" ");
    if (poruka.contains("ERROR")) {
      var status = new StatusUdaljenosti(400, poruka);
      var res = gson.toJson(status);
      return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

    } else {
      var res = gson.toJson(odvojeno[1]);
      return Response.ok().entity(res).build();
    }
  }

  /**
   * Endpoint za icao/udaljenost1/icao
   * 
   * @param icaoOd izvorisni aerodrom
   * @param icaoDo odredisni aerodrom
   * @return listu udaljenosti
   */
  @GET
  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenosti(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {

    Aerodrom ar1 = dohvatiAerodrom(icaoOd);
    Aerodrom ar2 = dohvatiAerodrom(icaoDo);

    String poruka = posaljiZahtjev(
        "UDALJENOST " + ar1.getLokacija().getLatitude() + " " + ar1.getLokacija().getLongitude()
            + " " + ar2.getLokacija().getLatitude() + " " + ar2.getLokacija().getLongitude());

    var odvojeno = poruka.split(" ");



    var gson = new Gson();

    if (poruka.contains("ERROR")) {
      var status = new StatusUdaljenosti(400, odvojeno[2]);
      var res = gson.toJson(status);
      return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    } else {

      double udaljenost = Double.parseDouble(odvojeno[1]);
      List<Aerodrom> lista = dohvatiAerodromeDrzave(ar2.getDrzava());
      List<UdaljenostDoAerodroma> uda = manjeUdaljenosti(ar1, lista, udaljenost);
      var res = gson.toJson(uda);
      return Response.ok().entity(res).build();
    }
  }

  /**
   * Endpoint za icao/udaljenost2
   * 
   * @param icaoOd izvorisni aerodrom
   * @param drzava drzava
   * @param km maksimalna kilometraza
   * @return listu aerodroma manjih od km unutar drzave
   */
  @GET
  @Path("{icaoOd }/udaljenost2")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenosti2(@PathParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") String km) {

    var gson = new Gson();
    if (!(drzava == null || km == null)) {
      Aerodrom ar1 = dohvatiAerodrom(icaoOd);
      List<Aerodrom> lista = dohvatiAerodromeDrzave(drzava);
      List<UdaljenostDoAerodroma> uda = manjeUdaljenosti(ar1, lista, Double.parseDouble(km));
      var res = gson.toJson(uda);
      return Response.ok().entity(res).build();
    } else {
      var status = new StatusUdaljenosti(400, "Nisu uneseni argumenti drzava i/ili km");
      var res = gson.toJson(status);
      return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }

  }



  /**
   * Metoda koja daje listu udaljenosti k
   * 
   * @param aerodrom pocetni aerodrom
   * @param lista lista aerodroma za usporedbu
   * @param udaljenost maksimalna udaljnost
   * @return udaljenosti onih aerodroma koji su manji od "udaljenosti"
   */
  private List<UdaljenostDoAerodroma> manjeUdaljenosti(Aerodrom aerodrom, List<Aerodrom> lista,
      double udaljenost) {
    List<UdaljenostDoAerodroma> trazeni = new ArrayList<>();

    for (Aerodrom aer : lista) {
      String poruka = posaljiZahtjev("UDALJENOST " + aerodrom.getLokacija().getLatitude() + " "
          + aerodrom.getLokacija().getLongitude() + " " + aer.getLokacija().getLatitude() + " "
          + aer.getLokacija().getLongitude());

      var razdvojeno = poruka.split(" ");
      if (Double.parseDouble(razdvojeno[1]) <= udaljenost) {
        Udaljenost u = new Udaljenost(aer.getDrzava(), Float.parseFloat(razdvojeno[1]));
        UdaljenostDoAerodroma noviAerodroma = new UdaljenostDoAerodroma(aer.getIcao(), u);
        trazeni.add(noviAerodroma);
      }
    }
    return trazeni;
  }


  /**
   * Metoda koja dohvaca aerodrome drzave
   * 
   * @param drzava
   * @return lista aerodroma
   */
  private List<Aerodrom> dohvatiAerodromeDrzave(String drzava) {

    String query = "SELECT * FROM AIRPORTS WHERE ISO_COUNTRY = ?";
    List<Aerodrom> aerodromi = new ArrayList<>();

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, drzava);

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
    return aerodromi;
  }

  /**
   * Metoda koaj dohvaca aerodrom prema icao
   * 
   * @param icao
   * @return aerodrom
   */
  private Aerodrom dohvatiAerodrom(String icao) {
    Aerodrom aerodrom = new Aerodrom();

    String query = "SELECT * FROM AIRPORTS WHERE ICAO = ?";

    PreparedStatement stmt = null;
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
        aerodrom = ad;
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
    return aerodrom;
  }

  /**
   * Metoda koja sluzi za slanje zahtjeva prema AP1
   * 
   * @param zahtjev
   * @return odgovor
   */
  private String posaljiZahtjev(String zahtjev) {

    var konfig = (Konfiguracija) context.getAttribute("konfig");
    var posluzitelj = konfig.dajPostavku("posluziteljUdaljenosti");
    var mreznaVrata = konfig.dajPostavku("mreznaVrata");
    var poruka = new StringBuilder();

    try {
      var mreznaUticnica = new Socket(posluzitelj, Integer.parseInt(mreznaVrata));
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();


      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        poruka.append(red);
      }
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "Pogreska Klijent: " + e.getMessage());//////////////////// TODO
    }
    return poruka.toString();
  }

}
