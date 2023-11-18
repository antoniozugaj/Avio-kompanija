package org.foi.nwtis.azugaj.aplikacija_2.rest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.Dnevnik;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * 
 * @author Antonio Žugaj
 * 
 *         Klasa koja sadrzi endpoint-ove za /dnevnik zahtjeve
 *
 */

@Path("dnevnik")
@RequestScoped
public class RestDnevnik {

  @Inject
  ServletContext context;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;


  /**
   * Endpoint za / putanju
   * 
   * @param vrsta - podatak za filtriranje vrstom
   * @param odBr - indeks pocetnog zapisa
   * @param br - broj zapisa po stranici
   * @return lista dnevnika
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajDnevnik(@QueryParam("vrsta") String vrsta,
      @DefaultValue("1") @QueryParam("odBroja") String odBr,
      @DefaultValue("20") @QueryParam("broj") String br) {

    var dnevnici = new ArrayList<Dnevnik>();

    String query = "";

    int broj;
    broj = Integer.parseInt(br);
    int odBroja;
    odBroja = Integer.parseInt(odBr);

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {

      if (vrsta != null) {
        query = "SELECT * FROM DNEVNIK WHERE VRSTA = ?";
        stmt = con.prepareStatement(query);
        stmt.setString(1, vrsta);
      } else {
        query = "SELECT * FROM DNEVNIK";
        stmt = con.prepareStatement(query);
      }
      ResultSet rs = stmt.executeQuery();
      int brojac = 0;
      while (rs.next()) {

        brojac++;
        if (brojac < odBroja)
          continue;

        if (brojac >= (odBroja + broj))
          break;

        String datum = rs.getString("DATUM");
        String akcija = rs.getString("AKCIJA");
        String vr = rs.getString("VRSTA");
        Dnevnik novi = new Dnevnik(datum, akcija, vr);
        dnevnici.add(novi);
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
    var jsonAerodrmi = gson.toJson(dnevnici);
    var odgovor = Response.ok().entity(jsonAerodrmi).build();

    return odgovor;
  }

  /**
   * Metoda za spremanje dnevnika
   * 
   * @param dnevnik
   * @return
   */
  @POST
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes({MediaType.APPLICATION_JSON})
  public Response postDnevnik(Dnevnik dnevnik) {


    String akcija = dnevnik.akcija();
    String vrsta = dnevnik.vrsta();

    String query = "INSERT INTO DNEVNIK (DATUM, AKCIJA, VRSTA) VALUES (CURRENT_TIMESTAMP, ?,?)";

    PreparedStatement stmt = null;
    try (var con = ds.getConnection()) {
      stmt = con.prepareStatement(query);
      stmt.setString(1, akcija);
      stmt.setString(2, vrsta);

      stmt.executeUpdate();
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
    return Response.ok().entity("Zapis spremljen").build();
  }


}

